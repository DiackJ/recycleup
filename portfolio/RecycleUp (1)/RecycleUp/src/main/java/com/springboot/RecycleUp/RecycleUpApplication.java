package com.springboot.RecycleUp;

import com.springboot.RecycleUp.Model.Account;
import com.springboot.RecycleUp.Model.Profile;
import com.springboot.RecycleUp.Repository.ProfileRepository;
import com.springboot.RecycleUp.Service.AccountService;
import com.springboot.RecycleUp.config.EnvLoader;
import org.springframework.beans.factory.annotation.Autowired;
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
