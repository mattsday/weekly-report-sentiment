package io.pivotal.mday.weekly.report.sentiment.model.weeklyreport;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Lob;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeeklyReportQuery {

	@JsonProperty("category")
	private List<String> category;

	@JsonProperty("customer")
	private List<String> customer;

	@ElementCollection(targetClass = String.class)
	@JsonProperty("pas")
	private List<String> pas;

	@JsonProperty("sales_play")
	private List<String> salesPlay;

	@Lob
	@JsonProperty("report_text")
	private List<String> reportText;

	@JsonProperty("sentiment")
	private List<Double> sentiment;

	@JsonProperty("date")
	private List<String> date;

	@JsonProperty("andor")
	private String andor;

	public String getAndor() {
		if (andor == null) {
			return "or";
		}
		return andor;
	}

}
