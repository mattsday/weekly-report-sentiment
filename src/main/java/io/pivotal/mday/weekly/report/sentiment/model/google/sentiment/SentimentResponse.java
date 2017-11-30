package io.pivotal.mday.weekly.report.sentiment.model.google.sentiment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "documentSentiment" })
@Data
public class SentimentResponse {

	@JsonProperty("documentSentiment")
	private DocumentSentiment documentSentiment;

}