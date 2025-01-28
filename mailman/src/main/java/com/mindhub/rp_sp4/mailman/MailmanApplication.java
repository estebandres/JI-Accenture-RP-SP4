package com.mindhub.rp_sp4.mailman;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static java.lang.Thread.sleep;

@SpringBootApplication
public class MailmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailmanApplication.class, args);
	}


	@Bean
	public CommandLineRunner dataLoader(EmailService emailService) {
		return args -> {
			sleep(5000);
			System.out.println("Sending email...");
			//emailService.sendEmail("morales.esteban.andres@gmail.com", "Subject", "Hello World!");
		};
	}
}
