package io.pivotal.mday.weekly.report.sentiment.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import io.pivotal.mday.weekly.report.sentiment.config.PlayMap;
import io.pivotal.mday.weekly.report.sentiment.model.google.charts.ChartTable;
import io.pivotal.mday.weekly.report.sentiment.model.google.charts.Col;
import io.pivotal.mday.weekly.report.sentiment.model.google.charts.Row;
import io.pivotal.mday.weekly.report.sentiment.model.google.charts.RowData;
import io.pivotal.mday.weekly.report.sentiment.model.weeklyreport.WeeklyReportEntry;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GoogleChartService {

	private PlayMap playMap;

	// Converts a collection to a standard data table
	public ChartTable reportToDataTable(List<WeeklyReportEntry> reports) {
		List<Row> rows = new ArrayList<>(100);
		ChartTable table = new ChartTable();
		table.setCols(getDataTableCols());
		table.setRows(rows);

		Collections.sort(reports, Collections.reverseOrder());
		for (WeeklyReportEntry e : reports) {
			Row row = new Row();
			row.add(new RowData(e.getDate()));
			row.add(new RowData(e.getCustomer()));
			row.add(new RowData(e.getSentiment()));
			row.add(new RowData(String.join(", ", e.getPas())));
			row.add(new RowData(e.getSalesPlay()));
			row.add(new RowData(e.getCategory()));
			row.add(new RowData(e.getReportText()));
			rows.add(row);
		}
		return table;
	}

	private List<Col> getDataTableCols() {
		return new ArrayList<Col>(Arrays.asList(new Col[] { new Col("A", "Date", "string"),
				new Col("B", "Customer", "string"), new Col("C", "Sentiment", "number"), new Col("D", "PA", "string"),
				new Col("E", "Sales Play", "string"), new Col("F", "Category", "string"),
				new Col("G", "Report Text", "string"), }));
	}

	public ChartTable reportToSalesPlay(List<WeeklyReportEntry> reports) {
		List<Row> rows = new ArrayList<>(reports.size());
		ChartTable table = new ChartTable();
		table.setCols(getSalesPlayCols());
		table.setRows(rows);
		int i = 1;
		for (WeeklyReportEntry e : reports) {
			Row row = new Row();
			row.add(new RowData(e.getCustomer()));
			row.add(new RowData("" + i++));
			row.add(new RowData("" + getPlay(e.getSalesPlay())));
			row.add(new RowData(e.getSalesPlay()));
			row.add(new RowData(e.getSentiment()));
			rows.add(row);
		}
		return table;

	}

	private int getPlay(String play) {
		int playNumber = -1;
		if (playMap.getPlays().containsKey(play)) {
			playNumber = playMap.getPlays().get(play);
		}
		return playNumber;
	}

	private List<Col> getSalesPlayCols() {
		return new ArrayList<Col>(Arrays.asList(new Col[] { new Col("A", "Customer", "string"),
				new Col("B", "Sequence", "number"), new Col("C", "Sentiment", "number"),
				new Col("D", "Sales Play", "string"), new Col("D", "Sentiment", "number"), }));
	}

	// Converts a collection to a table suitable for a line chart report
	public ChartTable reportToLineChartTable(List<WeeklyReportEntry> reports) {

		List<Row> rows = new ArrayList<>(reports.size());
		ChartTable table = new ChartTable();
		table.setCols(getLineChartCols());
		table.setRows(rows);

		Random random = new Random();

		// Date to store all the sentiment scores by date
		Map<String, List<Double>> sentiment = new HashMap<>();

		// Loop through all the reports and record just the scores + date
		for (WeeklyReportEntry e : reports) {
			List<Double> scores = sentiment.get(e.getDate());
			if (scores == null) {
				scores = new ArrayList<>(10);
				sentiment.put(e.getDate(), scores);
			}
			scores.add(e.getSentiment());
		}

		// Now sort the keys by date
		Object[] keys = sentiment.keySet().toArray();
		Arrays.sort(keys);

		// Extract it out, figure out the averages
		for (Object key : keys) {
			int nextInt = random.nextInt(256 * 256 * 256);
			String colour = String.format("#%06x", nextInt);
			List<Double> score = sentiment.get(key);
			Row row = new Row();
			row.add(new RowData((String) key));
			row.add(new RowData(getHigh(score), colour));
			row.add(new RowData(getLow(score), colour));
			row.add(new RowData(getMean(score), colour));
			rows.add(row);
		}

		return table;
	}

	private List<Col> getLineChartCols() {
		return new ArrayList<Col>(
				Arrays.asList(new Col[] { new Col("A", "Date", "string"), new Col("B", "Minimum", "number"),
						new Col("C", "Maximum", "number"), new Col("D", "Average", "number"), }));
	}

	private double getMean(List<Double> sentiment) {
		double sum = 0;
		for (double number : sentiment) {
			sum += number;
		}
		BigDecimal bd = new BigDecimal(sum / sentiment.size());
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	private double getHigh(List<Double> sentiment) {
		double high = -100;
		for (double number : sentiment) {
			if (number > high) {
				high = number;
			}
		}
		return high;
	}

	private double getLow(List<Double> sentiment) {
		double low = 100;
		for (double number : sentiment) {
			if (number < low) {
				low = number;
			}
		}
		return low;
	}
}
