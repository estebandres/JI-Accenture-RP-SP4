package com.mindhub.rp_sp3.eka_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EkaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EkaServerApplication.class, args);
	}

}
