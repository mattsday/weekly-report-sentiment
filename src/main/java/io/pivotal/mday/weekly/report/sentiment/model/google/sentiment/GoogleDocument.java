package io.pivotal.mday.weekly.report.sentiment.model.google.sentiment;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GoogleDocument {
	@JsonProperty("type")
	private String type;
	@JsonProperty("language")
	private String language;
	@JsonProperty("content")
	private String content;

	public GoogleDocument(String content) {
		this.type = "PLAIN_TEXT";
		this.language = "EN";
		this.content = content;
	}

}
