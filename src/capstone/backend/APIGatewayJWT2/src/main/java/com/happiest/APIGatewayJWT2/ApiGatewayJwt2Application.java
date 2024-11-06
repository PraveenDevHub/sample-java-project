package com.happiest.APIGatewayJWT2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableFeignClients
@CrossOrigin
public class ApiGatewayJwt2Application {

	public static void main(String[] args) {

		SpringApplication.run(ApiGatewayJwt2Application.class, args);
	}
}