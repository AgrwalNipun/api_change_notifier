package com.nipun.api_change_notifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ApiChangeNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiChangeNotifierApplication.class, args);
	}

}
