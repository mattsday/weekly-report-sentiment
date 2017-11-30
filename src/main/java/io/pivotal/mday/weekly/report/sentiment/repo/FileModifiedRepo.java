package io.pivotal.mday.weekly.report.sentiment.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.pivotal.mday.weekly.report.sentiment.model.google.drive.WeeklyReportFile;

public interface FileModifiedRepo extends JpaRepository<WeeklyReportFile, String> {
	public List<WeeklyReportFile> findByModifiedTime(String modifiedTime);

	public List<WeeklyReportFile> findById(String id);

}
