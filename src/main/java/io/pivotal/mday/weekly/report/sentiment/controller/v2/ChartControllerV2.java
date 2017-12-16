package io.pivotal.mday.weekly.report.sentiment.controller.v2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.mday.weekly.report.sentiment.model.google.charts.ChartTable;
import io.pivotal.mday.weekly.report.sentiment.model.weeklyreport.WeeklyReportEntry;
import io.pivotal.mday.weekly.report.sentiment.model.weeklyreport.WeeklyReportQuery;
import io.pivotal.mday.weekly.report.sentiment.repo.WeeklyReportRepo;
import io.pivotal.mday.weekly.report.sentiment.services.GoogleChartService;
import lombok.AllArgsConstructor;

@RequestMapping("/v2/chart")
@AllArgsConstructor
@RestController
public class ChartControllerV2 {

	private GoogleChartService chartService;
	private WeeklyReportRepo repo;

	private List<WeeklyReportEntry> findAll(WeeklyReportQuery query) {
		List<WeeklyReportEntry> results = new ArrayList<WeeklyReportEntry>((int) repo.count());
		boolean and = false;
		if ("and".equals(query.getAndor())) {
			and = true;
		}
		for (WeeklyReportEntry report : repo.findAll()) {
			// Check PA query
			if ((query.getPas() != null) && (query.getPas().size() > 0)) {
				int matches = checkPa(query.getPas(), report);
				if (and == true) {
					if (matches != query.getPas().size()) {
						continue;
					} else {
						if (matches == 0) {
							continue;
						}
					}
				} else {
					if (matches == 0) {
						continue;
					}
				}
			}
			if (!checkContains(query.getCustomer(), report.getCustomer())) {
				continue;
			}
			if (!checkContains(query.getDate(), report.getDate())) {
				continue;
			}
			if (!checkContains(query.getSentiment(), report.getSentiment())) {
				continue;
			}
			if (!checkContains(query.getCategory(), report.getCategory())) {
				continue;
			}
			if (!checkContains(query.getSalesPlay(), report.getSalesPlay())) {
				continue;
			}
			/*
			 * if ((query.getCustomer() != null) &&
			 * (!query.getCustomer().contains(report.getCustomer()))) {
			 * continue; } if ((query.getDate() != null) &&
			 * (!query.getDate().contains(report.getDate()))) { continue; } if
			 * ((query.getSentiment() != null) &&
			 * (!query.getSentiment().contains(report.getSentiment()))) {
			 * continue; } if ((query.getCategory() != null) &&
			 * (!query.getCategory().contains(report.getCategory()))) {
			 * continue; } if ((query.getSalesPlay() != null) &&
			 * (!query.getSalesPlay().contains(report.getSalesPlay()))) {
			 * continue; }
			 */
			results.add(report);
		}
		return results;
	}

	private boolean checkContains(List<String> list, String compare) {
		if (list == null) {
			return true;
		}
		if (list.size() == 0) {
			return true;
		}
		if (list.contains(compare)) {
			return true;
		}
		return false;
	}

	private boolean checkContains(List<Double> list, Double compare) {
		if (list == null) {
			return true;
		}
		if (list.size() == 0) {
			return true;
		}
		if (list.contains(compare)) {
			return true;
		}
		return false;
	}

	private int checkPa(List<String> paList, WeeklyReportEntry report) {
		if (paList.size() == 0) {
			return 0;
		}
		int i = 0;
		for (String pa : paList) {
			if (report.getPas().contains(pa)) {
				i++;
			}
		}
		return i;
	}

	@PostMapping("/table")
	public ChartTable postCustomerTable(@RequestBody WeeklyReportQuery entry) {
		return chartService.reportToDataTable(findAll(entry));
	}

	@PostMapping("/sentiment")
	public ChartTable postSentimentChart(@RequestBody WeeklyReportQuery entry) {
		return chartService.reportToLineChartTable(findAll(entry));
	}

	@PostMapping("/date")
	public ChartTable postDateChart(@RequestBody WeeklyReportQuery entry) {
		return chartService.reportToSalesPlay(findAll(entry));
	}

}
