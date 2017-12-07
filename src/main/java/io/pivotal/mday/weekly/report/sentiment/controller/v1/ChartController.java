package io.pivotal.mday.weekly.report.sentiment.controller.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.mday.weekly.report.sentiment.model.google.charts.ChartTable;
import io.pivotal.mday.weekly.report.sentiment.model.weeklyreport.ReportParams;
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

	@PostMapping("/table/request")
	public ChartTable postCustomerTable(@RequestBody ReportParams entry) {
		System.out.println(entry.toString());
		return null;
	}

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

	@GetMapping("/table/customer/{customer}/date/{date}")
	public ChartTable getCustomerTableForCustomerAndDate(@PathVariable("customer") String customer,
			@PathVariable("date") String date) {
		reportService.parseWeeklyReports();
		if (customer.equals("_all")) {
			return chartService.reportToDataTable(repo.findByDate(date));
		}
		return chartService.reportToDataTable(repo.findByCustomerIgnoreCaseAndDate(customer, date));
	}

	@GetMapping("/table/date/{date}")
	public ChartTable getCustomerTableDate(@PathVariable("date") String date) {
		reportService.parseWeeklyReports();
		return chartService.reportToDataTable(repo.findByDate(date));
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

	@GetMapping("/sentiment/customer/{customer}/date/{date}")
	public ChartTable getSentimentChartForCustomerAndDate(@PathVariable("customer") String customer,
			@PathVariable("date") String date) {
		reportService.parseWeeklyReports();
		if (customer.equals("_all")) {
			return chartService.reportToSalesPlay(repo.findByDate(date));
		}
		return chartService.reportToSalesPlay(repo.findByCustomerIgnoreCaseAndDate(customer, date));
	}

	@GetMapping("/sentiment/date/{date}")
	public ChartTable getSentimentChartForCustomerAndDate(@PathVariable("date") String date) {
		reportService.parseWeeklyReports();
		return chartService.reportToSalesPlay(repo.findByDate(date));
	}
}
