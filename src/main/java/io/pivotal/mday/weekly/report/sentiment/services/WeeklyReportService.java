package io.pivotal.mday.weekly.report.sentiment.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import io.pivotal.mday.weekly.report.sentiment.config.CustomerMutationMap;
import io.pivotal.mday.weekly.report.sentiment.config.PaMutationMap;
import io.pivotal.mday.weekly.report.sentiment.config.ReportsConfiguration;
import io.pivotal.mday.weekly.report.sentiment.model.google.drive.WeeklyReportFile;
import io.pivotal.mday.weekly.report.sentiment.model.weeklyreport.WeeklyReportEntry;
import io.pivotal.mday.weekly.report.sentiment.repo.FileModifiedRepo;
import io.pivotal.mday.weekly.report.sentiment.repo.WeeklyReportRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WeeklyReportService {

	private GoogleDriveService driveService;
	private GoogleSentimentService sentimentService;
	private ReportsConfiguration reportConfig;
	private WeeklyReportRepo reportRepo;
	private FileModifiedRepo fileRepo;
	private CustomerMutationMap customerMutationMap;
	private PaMutationMap paMutationMap;

	public void parseWeeklyReports() {
		// Get a list of weekly reports:
		for (WeeklyReportFile file : driveService.getWeeklyReports()) {
			// If the file's already been parsed, check if it's been cached
			if (fileRepo.exists(file.getId())) {
				WeeklyReportFile f = fileRepo.getOne(file.getId());
				if (file.getModifiedTime().equals(f.getModifiedTime())) {
					continue;
				}
			}
			// Get the date, this isn't as easy as it should be
			final String date = getReportDate(file.getName());
			// Ignore anything with an invalid date
			if (date == null) {
				System.out.println(file.getName() + " has a bad date!");
				continue;
			}
			// Get report contents
			final String contents = driveService.getFileContents(file.getId());
			if (contents != null) {
				// Parse the report
				parseWeeklyReport(contents, date);
				fileRepo.saveAndFlush(file);
			}
		}
	}

	private String getReportDate(String fileName) {
		// Use file name matcher to get date location in the report
		Pattern p = Pattern.compile(reportConfig.getPattern());
		Matcher m = p.matcher(fileName);

		if (!m.matches()) {
			return null;
		}
		final String date = m.group(1);

		// Because people are crap the date is all over the place, check
		// which one it is:
		p = Pattern.compile("(\\d{4}).(\\d{2}).(\\d{2})");
		m = p.matcher(date);
		String year = null, month = null, day = null;
		if (m.matches()) {
			// The format is likely YYYY-MM-DD
			year = m.group(1);
			month = m.group(2);
			day = m.group(3);
		} else {
			// The format is likely MM-DD-YYYY - the septics are on
			// their own if they want some other format
			p = Pattern.compile("(\\d{2}).(\\d{2}).(\\d{4})");
			m = p.matcher(date);
			if (m.matches()) {
				// The format is likely YYYY-MM-DD
				year = m.group(3);
				month = m.group(2);
				day = m.group(1);
			} else {
				// Can't figure out the date
				return null;
			}
		}
		return year + "-" + month + "-" + day;
	}

	private enum states {
		none, positive, negative, flat, poc
	};

	/**
	 * Scan a weekly reports contents and return a list of entries
	 */
	public void parseWeeklyReport(String fileContents, String date) {
		if (fileContents == null) {
			System.out.println(date + " file is empty!");
			return;
		}

		String[] lines = fileContents.split("\r\n");

		states state = states.none;

		// Store all the hashes we get
		List<String> hashes = new ArrayList<>(lines.length / 2);

		for (String line : lines) {
			final String hash = DigestUtils.sha1Hex(line);
			hashes.add(hash);

			// Have we been to this rodeo before? Eagerly discard
			if (!reportRepo.findByHash(hash).isEmpty()) {
				continue;
			}

			// Check the current state (none, positive, negative, flat or poc)
			// and change as needed:
			if ((state == states.none) && (line.matches(".*POSITIVE.*"))) {
				state = states.positive;
			} else if ((state == states.positive) && (line.matches(".*NEGATIVE.*"))) {
				state = states.negative;
			} else if ((state == states.negative) && (line.matches(".*FLAT.*"))) {
				state = states.flat;
			} else if ((state == states.flat) && (line.matches(".*POC Focus.*"))) {
				state = states.poc;
			}

			// If the state is none, continue
			if (state == states.none) {
				continue;
			}

			// If the line starts with a bullet '*' and is longer than 2
			// characters assume it's fair game
			if ((line.length() > 2) && (line.matches("^\\*.*"))) {
				// Lazily remove the first 2 characters (which should be '* ')
				WeeklyReportEntry entry = getReportEntry(line.substring(2), state);
				// Don't store any invalid lines
				if (entry == null) {
					continue;
				}
				// Attempt to get the sentiment:
				try {
					// Add the category to the content to give Google as much
					// context as possible (e.g. positive/negative etc)
					final String sentimentText = entry.getReportText() + " " + entry.getCategory();
					entry.setSentiment(sentimentService.getSentimentScore(sentimentText));
				} catch (Exception e) {
					// Set the sentiment to 0 if we fail
					entry.setSentiment(0d);
				}
				// Save a sha1 key of the whole entry
				entry.setHash(hash);
				// Save the date
				entry.setDate(date);
				reportRepo.saveAndFlush(entry);
			}
		}
		// Now cleanup
		for (WeeklyReportEntry e : reportRepo.findByDate(date)) {
			if (hashes.contains(e.getHash()))
				continue;
			reportRepo.delete(e.getHash());
		}
	}

	private WeeklyReportEntry getReportEntry(String line, states state) {
		WeeklyReportEntry entry = new WeeklyReportEntry();

		entry.setCategory(state.toString());

		int end = 0;

		// Pick out the PAs (start with @, can be more than one)
		Pattern paPattern = Pattern.compile("@(.*?)\\s");
		Matcher matcher = paPattern.matcher(line);
		List<String> pas = new ArrayList<String>(2);
		while (matcher.find()) {
			String paName = matcher.group(1);
			// Check against known mutations (e.g. Paname => PaName)
			if (paMutationMap.getPaMutation().containsKey(paName)) {
				paName = paMutationMap.getPaMutation().get(paName);
			}
			pas.add(paName);
			end = (matcher.end() > end) ? matcher.end() : end;
		}
		if (pas.size() == 0) {
			return null;
		}
		entry.setPas(pas);

		// Now pick out the customer (starts with a #, only one)
		Pattern customerPattern = Pattern.compile("#(.*?)\\s");
		matcher = customerPattern.matcher(line);

		if (matcher.find()) {
			String customerName = matcher.group(1);

			// Check against known mutations (e.g. Orwell => Ipagoo)
			if (customerMutationMap.getCustomerMutation().containsKey(customerName)) {
				customerName = customerMutationMap.getCustomerMutation().get(customerName);
			}
			entry.setCustomer(customerName);

			end = (matcher.end() > end) ? matcher.end() : end;
		} else {
			return null;
		}

		// Sales play (starts with a %, only one)
		Pattern salesPlayPattern = Pattern.compile("%(.*?)\\s");
		matcher = salesPlayPattern.matcher(line);

		if (matcher.find()) {
			// Convert sales play to lower case
			final String salesPlay = matcher.group(1).toLowerCase();
			entry.setSalesPlay(salesPlay);
			end = (matcher.end() > end) ? matcher.end() : end;
		} else {
			return null;
		}

		// Assume the metadata comes first, everything else is report text:
		entry.setReportText(line.substring(end));

		return entry;
	}

}
