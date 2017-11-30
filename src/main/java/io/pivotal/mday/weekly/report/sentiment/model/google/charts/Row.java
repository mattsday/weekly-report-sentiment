package io.pivotal.mday.weekly.report.sentiment.model.google.charts;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Row {
	@JsonProperty("c")
	private List<RowData> c;

	public Row() {
		this.c = new ArrayList<>();
	}

	public void add(RowData d) {
		this.c.add(d);
	}

}
