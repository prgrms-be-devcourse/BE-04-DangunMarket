package com.daangn.dangunmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
		basePackages = "com.daangn.dangunmarket",
		excludeFilters = @ComponentScan.Filter(
				type = FilterType.REGEX,
				pattern = "com\\.daangn\\.dangunmarket\\.domain\\.auth\\..*"
		)
)
public class DangunmarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(DangunmarketApplication.class, args);
	}

}
