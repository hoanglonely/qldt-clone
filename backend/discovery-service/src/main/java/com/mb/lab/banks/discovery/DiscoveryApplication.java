package com.mb.lab.banks.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(DiscoveryApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
	}

}
