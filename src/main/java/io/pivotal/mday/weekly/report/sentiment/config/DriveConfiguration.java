package io.pivotal.mday.weekly.report.sentiment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DriveConfiguration {

	@Value("${googleapi.drive.list.uri}")
	private String fileListUri;

	@Value("${googleapi.drive.get.uri}")
	private String fileGetUri;

	public String getFileListUri(String folder) {
		return fileListUri.replace("{folder}", folder);
	}

	public void setFileListUri(String fileListUri) {
		this.fileListUri = fileListUri;
	}

	public String getFileGetUri(String id) {
		return fileGetUri.replace("{id}", id);
	}

	public void setFileGetUri(String fileGetUri) {
		this.fileGetUri = fileGetUri;
	}

}
