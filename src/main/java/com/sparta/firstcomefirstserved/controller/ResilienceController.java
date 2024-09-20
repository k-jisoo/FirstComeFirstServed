package com.sparta.firstcomefirstserved.controller;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class ResilienceController {
    private final WebClient webClient;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public ResilienceController() {
        // WebClient 설정
        this.webClient = WebClient.builder()
                .baseUrl("http://errorExample") // 서비스 A의 URL로 변경
                .build();

        // Circuit Breaker 설정
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slidingWindowSize(10)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .minimumNumberOfCalls(5)
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("webClientCircuitBreaker");

        // Retry 설정
        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofSeconds(2))
                .build();

        RetryRegistry retryRegistry = RetryRegistry.of(retryConfig);
        this.retry = retryRegistry.retry("webClientRetry");
    }

    @GetMapping("/resilient/case1")
    public Mono<String> callCase1() {
        return WebClient.builder()
                .baseUrl("errorExample")
                .build()
                .get()
                .uri("/errorful/case1")
                .retrieve()
                .bodyToMono(String.class)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker)) // Circuit Breaker 적용
                .transformDeferred(RetryOperator.of(retry)); // Retry 적용
    }

    @GetMapping("/resilient/case2")
    public Mono<String> callCase2() {
        return WebClient.builder()
                .baseUrl("errorExample")
                .build()
                .get()
                .uri("/errorful/case2")
                .retrieve()
                .bodyToMono(String.class)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .transformDeferred(RetryOperator.of(retry));
    }

    @GetMapping("/resilient/case3")
    public Mono<String> callCase3() {
        return WebClient.builder()
                .baseUrl("errorExample")
                .build()
                .get()
                .uri("/errorful/case3")
                .retrieve()
                .bodyToMono(String.class)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .transformDeferred(RetryOperator.of(retry));
    }
}