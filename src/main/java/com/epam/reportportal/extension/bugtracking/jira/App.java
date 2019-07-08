package com.epam.reportportal.extension.bugtracking.jira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
@SpringBootApplication(scanBasePackages = "com.epam", exclude = { MultipartAutoConfiguration.class, FlywayAutoConfiguration.class })
@Import({ com.epam.ta.reportportal.config.DatabaseConfiguration.class })
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
