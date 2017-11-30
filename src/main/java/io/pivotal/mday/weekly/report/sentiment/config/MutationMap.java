package io.pivotal.mday.weekly.report.sentiment.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "reports")
public class MutationMap {
	private Map<String, String> mutation;

	public Map<String, String> getMutation() {
		return mutation;
	}

	public void setMutation(Map<String, String> mutationMap) {
		this.mutation = mutationMap;
	}

}
