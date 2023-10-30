package com.dcits.saas.gateway.route;

import cn.hutool.core.lang.id.NanoId;
import com.dcits.saas.gateway.constants.GatewayConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Klaus
 * @since 2023/10/27
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RouteRefreshProducer {

	private final ReactiveRedisOperations<String, String> redisOperations;

	public Mono<Long> publishRefresh() {
		log.info("Sent refresh routes message.");
		return redisOperations.convertAndSend(GatewayConstants.TOPIC_REFRESH_ROUTES, "refresh");
	}

}
