package io.pivotal.mday.weekly.report.sentiment.model.weeklyreport;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class WeeklyReportEntry implements Comparable<WeeklyReportEntry> {
	@Id
	@GeneratedValue
	@JsonIgnore
	private Long id;

	@JsonProperty("category")
	private String category;

	@JsonProperty("customer")
	private String customer;

	@ElementCollection(targetClass = String.class)
	@JsonProperty("pas")
	private List<String> pas;

	@JsonProperty("sales_play")
	private String salesPlay;

	@Lob
	@JsonProperty("report_text")
	private String reportText;

	@JsonProperty("sentiment")
	private Double sentiment;

	@JsonProperty("date")
	private String date;

	@JsonIgnore
	private String hash;

	@Override
	public int compareTo(WeeklyReportEntry c) {
		final int myDate = formatDate(this.date);
		final int theirDate = formatDate(c.getDate());
		if ((myDate == 0) || (theirDate == 0)) {
			// Return 0 if uncertain
			return 0;
		}
		if (myDate == theirDate) {
			return 0;
		} else if (myDate > theirDate) {
			return 1;
		} else {
			return -1;
		}
	}

	private int formatDate(String date) {
		try {
			return Integer.parseInt(date.replaceAll("-", ""));
		} catch (Exception e) {
			return 0;
		}
	}
}
