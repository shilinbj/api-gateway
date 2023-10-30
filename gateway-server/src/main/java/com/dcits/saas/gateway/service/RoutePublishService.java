package com.dcits.saas.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * Publish routes.
 *
 * @author Klaus
 * @since 2023/10/27
 */
@Service
@Slf4j
public class RoutePublishService implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher publisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

	public void publishRoutes() {
		log.info("Triggered publish routes event.");
		publisher.publishEvent(new RefreshRoutesEvent(this));
	}

}
