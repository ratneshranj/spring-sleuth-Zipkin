package com.gateway;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class GatewayApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(GatewayApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
	
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		logger.debug("Star myRoutes ");
		return builder.routes()
		        .route(p -> p
		            .path("/bank/account/**")
		            .filters(f -> f.rewritePath("/bank/account/(?<segment>.*)","/${segment}")
		            				.addResponseHeader("X-Response-Time",new Date().toString()))
		            .uri("lb://ACCOUNT")).
		        route(p -> p
			            .path("/bank/loans/**")
			            .filters(f -> f.rewritePath("/bank/loans/(?<segment>.*)","/${segment}")
			            		.addResponseHeader("X-Response-Time",new Date().toString()))
			            .uri("lb://LOANS")).
		        route(p -> p
			            .path("/bank/card/**")
			            .filters(f -> f.rewritePath("/bank/card/(?<segment>.*)","/${segment}")
			            		.addResponseHeader("X-Response-Time",new Date().toString()))
			            .uri("lb://CARD")).build();
		
	}

}
