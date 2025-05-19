package com.hyperskill.customerFeedback;

import com.hyperskill.customerFeedback.Config.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("com.hyperskill.customerFeedback.Repository")
public class CustomerFeedbackApplication {
	static{
		EnvLoader.loadEnv();
	}

	public static void main(String[] args) {
		SpringApplication.run(CustomerFeedbackApplication.class, args);
	}

}
