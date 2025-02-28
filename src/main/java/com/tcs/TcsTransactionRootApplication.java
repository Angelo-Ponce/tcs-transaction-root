package com.tcs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import reactor.core.publisher.Mono;

@EnableDiscoveryClient
@EnableR2dbcAuditing
@SpringBootApplication
public class TcsTransactionRootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TcsTransactionRootApplication.class, args);
    }

    @Bean
    public ReactiveAuditorAware<String> auditorAware() {
        return () -> Mono.just("Angelo"); // Reemplaza "system" con el usuario actual
    }

}
