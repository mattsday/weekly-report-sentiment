package io.pivotal.mday.weekly.report.sentiment.controller.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	@GetMapping("/customers")
	public List<String> listCustomers() {
		reportService.parseWeeklyReports();
		Set<String> customers = new HashSet<>();
		for (WeeklyReportEntry e : repo.findAll()) {
			customers.add(e.getCustomer());
		}
		List<String> sortedCustomers = new ArrayList<>(customers);
		Collections.sort(sortedCustomers);
		return sortedCustomers;
	}

	@GetMapping("/dates")
	public List<String> listDates() {
		reportService.parseWeeklyReports();
		Set<String> dates = new HashSet<>();
		for (WeeklyReportEntry e : repo.findAll()) {
			dates.add(e.getDate());
		}
		List<String> sortedDates = new ArrayList<>(dates);
		Collections.sort(sortedDates);
		return sortedDates;
	}

	@GetMapping("/plays")
	public List<String> listPlays() {
		reportService.parseWeeklyReports();
		Set<String> plays = new HashSet<>();
		for (WeeklyReportEntry e : repo.findAll()) {
			plays.add(e.getSalesPlay());
		}
		List<String> sortedPlays = new ArrayList<>(plays);
		Collections.sort(sortedPlays);
		return sortedPlays;
	}

	@GetMapping("/pas")
	public List<String> listPas() {
		reportService.parseWeeklyReports();
		Set<String> pas = new HashSet<>();
		for (WeeklyReportEntry e : repo.findAll()) {
			pas.addAll(e.getPas());
		}
		List<String> sortedPas = new ArrayList<>(pas);
		Collections.sort(sortedPas);
		return sortedPas;
	}

	@GetMapping("/sentiment")
	public List<Double> listSentiment() {
		reportService.parseWeeklyReports();
		Set<Double> sentiment = new HashSet<>();
		for (WeeklyReportEntry e : repo.findAll()) {
			sentiment.add(e.getSentiment());
		}
		List<Double> sortedSentiment = new ArrayList<>(sentiment);
		Collections.sort(sortedSentiment);
		return sortedSentiment;
	}

	@GetMapping("/categories")
	public List<String> listCategories() {
		return Arrays.asList(new String[] { "positive", "negative", "flat", "poc" });
	}
}
