package io.pivotal.mday.weekly.report.sentiment.model.google.charts;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ChartTable {
	@NonNull
	@JsonProperty("cols")
	public List<Col> cols;

	@NonNull
	@JsonProperty("rows")
	public List<Row> rows;
}
