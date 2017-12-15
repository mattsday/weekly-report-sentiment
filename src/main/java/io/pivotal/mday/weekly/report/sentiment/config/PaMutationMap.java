package io.pivotal.mday.weekly.report.sentiment.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "reports")
@Data
public class PaMutationMap {
	private Map<String, String> paMutation;
}
