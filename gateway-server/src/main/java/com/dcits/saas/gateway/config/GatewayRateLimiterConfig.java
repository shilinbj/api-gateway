package com.dcits.saas.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 限流配置
 *
 * @author Klaus
 * @since 2023/10/25
 */
@Configuration
public class GatewayRateLimiterConfig {

	/**
	 * 来源IP限流
	 *
	 * @return
	 */
	@Primary
	@Bean("ipKeyResolver")
	public KeyResolver ipKeyResolver() {
		return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
	}

	/**
	 * 接口限流
	 *
	 * @return
	 */
	@Bean("apiKeyResolver")
	public KeyResolver apiKeyResolver() {
		return exchange -> Mono.just(exchange.getRequest().getPath().value());
	}

	/**
	 * 用户限流
	 */
	@Bean(value = "userKeyResolver")
	public KeyResolver userKeyResolver() {
		return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getHeaders().getFirst("Authorization")));
	}

}
