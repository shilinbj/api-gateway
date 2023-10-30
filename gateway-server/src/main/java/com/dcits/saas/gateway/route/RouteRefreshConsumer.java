package com.dcits.saas.gateway.route;

import com.dcits.saas.gateway.service.RoutePublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import static com.dcits.saas.gateway.constants.GatewayConstants.TOPIC_REFRESH_ROUTES;

/**
 * @author Klaus
 * @since 2023/10/27
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class RouteRefreshConsumer {

	private final RoutePublishService publishService;

	@Bean
	public ReactiveRedisMessageListenerContainer container(ReactiveRedisConnectionFactory factory) {
		ReactiveRedisMessageListenerContainer container = new ReactiveRedisMessageListenerContainer(factory);
		container.receive(ChannelTopic.of(TOPIC_REFRESH_ROUTES))
				.map(ReactiveSubscription.Message::getMessage)
				.subscribe(msg -> {
					log.info("Received refresh routes message: {}", msg);
					if ("refresh".equals(msg)) {
						publishService.publishRoutes();
					}
				});
		return container;
	}

}
