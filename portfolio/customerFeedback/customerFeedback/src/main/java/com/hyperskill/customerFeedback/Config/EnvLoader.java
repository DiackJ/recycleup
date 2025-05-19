package com.hyperskill.customerFeedback.Config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {

    public static void loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .filename(".env")
                .load();

        System.setProperty("DB_DATABASE", dotenv.get("DB_DATABASE"));
        System.setProperty("DB_URI", dotenv.get("DB_URI"));
    }
}
