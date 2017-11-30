package io.pivotal.mday.weekly.report.sentiment.controller.v1;

import java.util.Collection;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.mday.weekly.report.sentiment.model.weeklyreport.WeeklyReportEntry;
import io.pivotal.mday.weekly.report.sentiment.repo.WeeklyReportRepo;
import io.pivotal.mday.weekly.report.sentiment.services.WeeklyReportService;
import lombok.AllArgsConstructor;

@RequestMapping("/v1")
@RestController
@AllArgsConstructor
public class WeeklyReportController {

	private WeeklyReportService reportService;
	private WeeklyReportRepo repo;

	@GetMapping("/init")
	public String test() {
		reportService.parseWeeklyReports();
		return "Initialised!";
	}

	@GetMapping("/list")
	public List<WeeklyReportEntry> listAll() {
		return repo.findAll();
	}

	@GetMapping("/list/customer/{customer}")
	public Collection<WeeklyReportEntry> listByCustomer(
			@PathVariable(name = "customer", required = true) String customer) {
		return repo.findByCustomerIgnoreCase(customer);
	}

	@GetMapping("/list/category/{category}")
	public Collection<WeeklyReportEntry> listByCategory(
			@PathVariable(name = "category", required = true) String customer) {
		return repo.findByCategoryIgnoreCase(customer);
	}

}
