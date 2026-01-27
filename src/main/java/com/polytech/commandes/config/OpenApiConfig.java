package com.polytech.commandes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gestion de Commandes")
                        .version("1.0.0")
                        .description(
                                "API REST pour la gestion de commandes clients.\n\n" +
                                        "**Profil actif:** " + activeProfile + "\n\n" +
                                        "**Prérequis:**\n" +
                                        "- Base de données MySQL configurée\n" +
                                        "- Variables d'environnement définies (DB_URL, DB_USERNAME, DB_PASSWORD)"
                        )
                        .contact(new Contact()
                                .name("Institut Polytechnique de Saint Louis")
                                .email("support@polytech.sn")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Serveur local")
                ));
    }
}