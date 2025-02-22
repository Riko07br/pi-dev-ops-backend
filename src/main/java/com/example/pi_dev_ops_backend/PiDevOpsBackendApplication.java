package com.example.pi_dev_ops_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableSpringDataWebSupport (pageSerializationMode = VIA_DTO)
@SpringBootApplication
@EnableMethodSecurity
public class PiDevOpsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PiDevOpsBackendApplication.class, args);
	}

}
