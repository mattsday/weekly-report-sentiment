package io.pivotal.mday.weekly.report.sentiment.controller.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.mday.weekly.report.sentiment.model.google.charts.ChartTable;
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

	@GetMapping("/table")
	public ChartTable getCustomerTable() {
		reportService.parseWeeklyReports();
		return chartService.reportToDataTable(repo.findAll());
	}

	@GetMapping("/table/customer/{customer}")
	public ChartTable getCustomerTableForCustomer(@PathVariable("customer") String customer) {
		reportService.parseWeeklyReports();
		return chartService.reportToDataTable(repo.findByCustomerIgnoreCase(customer));
	}

	@GetMapping("/sentiment")
	public ChartTable getSentimentChart() {
		reportService.parseWeeklyReports();
		return chartService.reportToLineChartTable(repo.findAll());
	}

	@GetMapping("/sentiment/customer/{customer}")
	public ChartTable getSentimentChartForCustomer(@PathVariable("customer") String customer) {
		reportService.parseWeeklyReports();
		return chartService.reportToLineChartTable(repo.findByCustomerIgnoreCase(customer));
	}
}
