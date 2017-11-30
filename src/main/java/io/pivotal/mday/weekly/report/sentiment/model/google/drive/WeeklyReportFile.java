package io.pivotal.mday.weekly.report.sentiment.model.google.drive;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Data
public class WeeklyReportFile implements Comparable<WeeklyReportFile> {
	@Id
	@JsonProperty("id")
	private String id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("mimeType")
	private String mimeType;

	@JsonProperty("modifiedTime")
	private String modifiedTime;

	@Override
	public int compareTo(WeeklyReportFile c) {
		final int myDate = getDate(this.getName());
		final int theirDate = getDate(c.getName());
		if ((myDate == 0) || (theirDate == 0)) {
			// Return 0 if uncertain
			return 0;
		}
		if (myDate == theirDate) {
			return 0;
		} else if (myDate > theirDate) {
			return 1;
		} else {
			return -1;
		}

	}

	private int getDate(String fileName) {
		// Because people are crap the date is all over the place, check
		// which one it is:
		Pattern p = Pattern.compile(".*(\\d{4}).(\\d{2}).(\\d{2}).*");
		Matcher m = p.matcher(fileName);
		String year = null, month = null, day = null;
		if (m.matches()) {
			// The format is likely YYYY-MM-DD
			year = m.group(1);
			month = m.group(2);
			day = m.group(3);
		} else {
			// The format is likely MM-DD-YYYY - the septics are on
			// their own if they want some other format
			p = Pattern.compile(".*(\\d{2}).(\\d{2}).(\\d{4}).*");
			m = p.matcher(fileName);
			if (m.matches()) {
				// The format is likely YYYY-MM-DD
				year = m.group(3);
				month = m.group(2);
				day = m.group(1);
			} else {
				// Can't figure out the date
				System.out.println("Can't figure out date from " + fileName);
				return 0;
			}
		}
		try {
			return Integer.parseInt(year + month + day);
		} catch (Exception e) {
			return 0;
		}
	}

}
