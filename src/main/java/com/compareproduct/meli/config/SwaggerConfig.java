package com.compareproduct.meli.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("https://miguelprogrammer-challenge.duckdns.org").description("Servidor de Produção"))
                .info(new Info()
                        .title("Product Comparison API")
                        .version("1.0.0")
                        .description("API para comparação de produtos")
                        .contact(new Contact()
                                .name("Dev Team")
                                .email("dev@compareproduct.com")));
    }
}