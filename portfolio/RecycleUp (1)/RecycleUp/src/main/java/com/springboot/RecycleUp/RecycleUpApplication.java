package com.springboot.RecycleUp;


import com.springboot.RecycleUp.config.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RecycleUpApplication {
	static{
		EnvLoader.loadEnv(); //loads system properties from env at start of application
	}


	public static void main(String[] args) {
		SpringApplication.run(RecycleUpApplication.class, args);

	}

}
