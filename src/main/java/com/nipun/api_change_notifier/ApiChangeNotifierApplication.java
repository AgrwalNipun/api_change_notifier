package com.nipun.api_change_notifier;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.nipun.api_change_notifier.services.FileIteratingService;
import com.nipun.api_change_notifier.services.ParsingService;

@SpringBootApplication
@EnableJpaAuditing
public class ApiChangeNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiChangeNotifierApplication.class, args);
		// ParsingService ps = new ParsingService();
		// ps.parse();

		FileIteratingService s = new FileIteratingService();
		s.scanJavaFiles(new File("C:\\Users\\n" + //
						"ipun\\Desktop\\spring boot\\n" + //
						"ip"));
	}

}
 