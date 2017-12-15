package io.pivotal.mday.weekly.report.sentiment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class ReportsConfiguration {

	@Value("${reports.folder}")
	private String folder;

	@Value("${reports.pattern}")
	private String pattern;

}
