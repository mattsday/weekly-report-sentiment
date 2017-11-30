package io.pivotal.mday.weekly.report.sentiment.model.google.drive;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class FilesList {
	@JsonProperty("files")
	private ArrayList<WeeklyReportFile> files;
}
