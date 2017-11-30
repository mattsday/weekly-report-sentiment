package io.pivotal.mday.weekly.report.sentiment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentimentConfiguration {

	@Value("${googleapi.sentiment.uri}")
	private String sentimenturi;

	public String getSentimenturi() {
		return sentimenturi;
	}

	public void setSentimenturi(String sentimenturi) {
		this.sentimenturi = sentimenturi;
	}

}
