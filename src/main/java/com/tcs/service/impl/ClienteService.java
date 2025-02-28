package com.tcs.service.impl;

import com.tcs.dto.ClientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final WebClient webClient;

    public Mono<ClientDTO> findByClientId(String clienteId, String authToken) {
        return webClient.get()
                .uri("/{clienteId}", clienteId)
                .header("Authorization", authToken)
                .retrieve()
                .bodyToMono(ClientDTO.class);
    }
}
