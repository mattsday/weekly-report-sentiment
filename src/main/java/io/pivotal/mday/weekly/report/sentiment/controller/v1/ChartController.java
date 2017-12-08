package io.pivotal.mday.weekly.report.sentiment.controller.v1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.mday.weekly.report.sentiment.model.google.charts.ChartTable;
import io.pivotal.mday.weekly.report.sentiment.model.weeklyreport.WeeklyReportEntry;
import io.pivotal.mday.weekly.report.sentiment.repo.WeeklyReportRepo;
import io.pivotal.mday.weekly.report.sentiment.services.GoogleChartService;
import io.pivotal.mday.weekly.report.sentiment.services.WeeklyReportService;
import lombok.AllArgsConstructor;

@RequestMapping("/v1/chart")
@AllArgsConstructor
@RestController
public class ChartController {

	private GoogleChartService chartService;
	private WeeklyReportRepo repo;
	private WeeklyReportService reportService;

	private List<WeeklyReportEntry> findAll(WeeklyReportEntry entry) {
		reportService.parseWeeklyReports();
		Example<WeeklyReportEntry> example = Example.of(entry);
		List<WeeklyReportEntry> results = repo.findAll(example);
		if (entry.getPas() == null) {
			return results;
		}
		// JPA queries don't filter out arrays
		List<WeeklyReportEntry> filtered = new ArrayList<>(results.size());
		for (WeeklyReportEntry e : results) {
			int match = entry.getPas().size();
			for (String pa : entry.getPas()) {
				if (e.getPas().contains(pa)) {
					match--;
				}
			}
			if (match == 0) {
				filtered.add(e);
			}
		}
		return filtered;
	}

	@PostMapping("/table")
	public ChartTable postCustomerTable(@RequestBody WeeklyReportEntry entry) {
		return chartService.reportToDataTable(findAll(entry));
	}

	@PostMapping("/sentiment")
	public ChartTable postSentimentChart(@RequestBody WeeklyReportEntry entry) {
		return chartService.reportToLineChartTable(findAll(entry));
	}

	@PostMapping("/date")
	public ChartTable postDateChart(@RequestBody WeeklyReportEntry entry) {
		return chartService.reportToSalesPlay(findAll(entry));
	}

}
