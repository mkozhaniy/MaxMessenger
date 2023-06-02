package com.maxprojects.maxmessenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MaxMessengerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MaxMessengerApplication.class, args);
    }

}
