package io.pivotal.mday.weekly.report.sentiment.model.weeklyreport;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReportParams {
	@JsonProperty("customer")
	private String customer;
	@JsonProperty("date")
	private String date;
}
