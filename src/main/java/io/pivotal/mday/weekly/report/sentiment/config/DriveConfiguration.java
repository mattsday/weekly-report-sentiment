package io.pivotal.mday.weekly.report.sentiment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Setter;

@Configuration
public class DriveConfiguration {

	@Value("${googleapi.drive.list.uri}")
	@Setter
	private String fileListUri;

	@Value("${googleapi.drive.get.uri}")
	@Setter
	private String fileGetUri;

	public String getFileListUri(String folder) {
		return fileListUri.replace("{folder}", folder);
	}

	public String getFileGetUri(String id) {
		return fileGetUri.replace("{id}", id);
	}

}
