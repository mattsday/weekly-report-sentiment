package io.pivotal.mday.weekly.report.sentiment.model.google.charts;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Col {
	@JsonProperty("id")
	private String id;

	@JsonProperty("label")
	private String label;

	@JsonProperty("type")
	private String type;

}
