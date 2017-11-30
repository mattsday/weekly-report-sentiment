package io.pivotal.mday.weekly.report.sentiment.model.google.charts;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RowData {
	@JsonProperty("v")
	private String v;

	@JsonProperty("f")
	private String f;

	@JsonProperty("p")
	private String p;

	public RowData() {
	}

	public RowData(String data) {
		this.v = data;
		this.f = data;
	}

	public RowData(Double data) {
		this.v = data.toString();
		this.f = data.toString();
	}

	public RowData(String data, String p) {
		this.v = data;
		this.f = data;
		this.p = p;
	}

	public RowData(Double data, String p) {
		this.v = data.toString();
		this.f = data.toString();
		this.p = p;
	}

}
