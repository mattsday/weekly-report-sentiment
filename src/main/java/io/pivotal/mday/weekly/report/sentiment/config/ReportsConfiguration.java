package io.pivotal.mday.weekly.report.sentiment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportsConfiguration {

	@Value("${reports.folder}")
	private String folder;
	
	@Value("${reports.pattern}")
	private String pattern;

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}


}
