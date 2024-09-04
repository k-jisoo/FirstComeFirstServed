package com.sparta.firstcomefirstserved;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class FirstcomefirstservedApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstcomefirstservedApplication.class, args);
	}

}
