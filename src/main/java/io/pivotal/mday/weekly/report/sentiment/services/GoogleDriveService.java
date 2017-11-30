package io.pivotal.mday.weekly.report.sentiment.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import io.pivotal.mday.weekly.report.sentiment.config.DriveConfiguration;
import io.pivotal.mday.weekly.report.sentiment.config.ReportsConfiguration;
import io.pivotal.mday.weekly.report.sentiment.model.google.drive.FilesList;
import io.pivotal.mday.weekly.report.sentiment.model.google.drive.WeeklyReportFile;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GoogleDriveService {
	private OAuth2RestTemplate restTemplate;

	private DriveConfiguration driveConfiguration;
	private ReportsConfiguration reportsConfiguration;

	public List<WeeklyReportFile> getWeeklyReports() {
		List<WeeklyReportFile> reports = new ArrayList<>(10);
		Pattern p = Pattern.compile(reportsConfiguration.getPattern());
		// Get everything in the folder and filter it out
		for (WeeklyReportFile file : listFiles(reportsConfiguration.getFolder()).getFiles()) {
			Matcher m = p.matcher(file.getName());
			if (m.matches()) {
				reports.add(file);
			}
		}
		return reports;
	}

	// TODO - add multi page support (e.g. if it returns "incompleteSearch":
	// true)
	public FilesList listFiles(String folder) {
		return restTemplate.getForObject(driveConfiguration.getFileListUri(folder), FilesList.class);
	}

	public String getFileContents(String id) {
		try {
			return restTemplate.getForObject(driveConfiguration.getFileGetUri(id), String.class);
		} catch (Exception e) {
			// Just return nothing if we get an error like 403 etc
			return null;
		}
	}

}
