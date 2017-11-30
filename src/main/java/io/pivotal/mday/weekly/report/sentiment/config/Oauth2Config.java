package io.pivotal.mday.weekly.report.sentiment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

@Configuration
public class Oauth2Config {
	/*
	 * Spring doesn't automatically autowire the OAuth2 client context and
	 * resource configuration - do so here so we can use it to make rest calls
	 */
	@Bean
	public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
			OAuth2ProtectedResourceDetails details) {
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(details, oauth2ClientContext);
		return restTemplate;
	}

}
