package io.pivotal.mday.weekly.report.sentiment.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.pivotal.mday.weekly.report.sentiment.model.weeklyreport.WeeklyReportEntry;

public interface WeeklyReportRepo extends JpaRepository<WeeklyReportEntry, Long> {
	public List<WeeklyReportEntry> findByHash(String hash);

	public List<WeeklyReportEntry> findByCustomerIgnoreCase(String customer);

	public List<WeeklyReportEntry> findByCustomerIgnoreCaseAndDate(String customer, String date);

	public List<WeeklyReportEntry> findByCategoryIgnoreCase(String category);

	public List<WeeklyReportEntry> findByDate(String date);
}
