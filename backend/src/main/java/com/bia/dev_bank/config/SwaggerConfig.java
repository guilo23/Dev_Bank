package com.bia.dev_bank.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("devbank-public")
        .pathsToMatch("/**")
        .build();
  }

  @Bean
  public OpenAPI customApi() {
    return new OpenAPI()
        .info(new Info()
            .title("DevBank API")
            .version("1.0")
            .description(
                "REST API for DevBank backend services, providing endpoints for customer management, accounts, transactions, and more."));
  }
}
