package com.springboot.RecycleUp.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.nio.file.Paths;

public class EnvLoader {
    public static void loadEnv() {
        Dotenv dotenv;
        try{
            dotenv = Dotenv.configure()
                    .directory("../") //tests
                    .filename(".env")
                    .load();
        }catch(Exception e){
            dotenv = Dotenv.configure()
                    .directory("./") //main app
                    .filename(".env")
                    .load();
        }

        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
    }
}
