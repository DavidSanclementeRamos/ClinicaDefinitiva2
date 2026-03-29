package com.example.ClinicaDefinitiva;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ClinicaDefinitivaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicaDefinitivaApplication.class, args);

		Logger logger = LoggerFactory.getLogger(ClinicaDefinitivaApplication.class);
		logger.info("Logger funciona correctamente");
		System.out.print(" holo desde Spring");
	}
        
     /**   @Component
public class HashGenerator implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("HASH: " + new BCryptPasswordEncoder().encode("admin123"));
    }
}*/


// http://localhost/phpmyadmin/index.php?route=/sql&pos=0&db=prueva&tab

//C:/Users/David/OneDrive/Documents/proyectosJava/ClinicaDefinitiva

}