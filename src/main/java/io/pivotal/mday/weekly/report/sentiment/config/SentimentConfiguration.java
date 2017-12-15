package io.pivotal.mday.weekly.report.sentiment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class SentimentConfiguration {

	@Value("${googleapi.sentiment.uri}")
	private String sentimenturi;

}
