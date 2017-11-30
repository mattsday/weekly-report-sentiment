package io.pivotal.mday.weekly.report.sentiment.model.google.sentiment;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SentimentSubmission {
	@JsonProperty("document")
	private GoogleDocument document;

	public SentimentSubmission(String content) {
		this.document = new GoogleDocument(content);
	}
}
