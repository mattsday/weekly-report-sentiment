package io.pivotal.mday.weekly.report.sentiment.services;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import io.pivotal.mday.weekly.report.sentiment.config.SentimentConfiguration;
import io.pivotal.mday.weekly.report.sentiment.model.google.sentiment.SentimentResponse;
import io.pivotal.mday.weekly.report.sentiment.model.google.sentiment.SentimentSubmission;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GoogleSentimentService {
	private OAuth2RestTemplate restTemplate;
	private SentimentConfiguration sentimentConfiguration;

	public SentimentResponse getSentiment(String content) {
		SentimentSubmission s = new SentimentSubmission(content);
		return restTemplate.postForObject(sentimentConfiguration.getSentimenturi(), s, SentimentResponse.class);
	}

	public double getSentimentScore(String content) {
		return getSentiment(content).getDocumentSentiment().getScore();
	}
}
