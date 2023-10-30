package com.dcits.saas.gateway.service;

import cn.hutool.core.lang.id.NanoId;
import com.dcits.saas.gateway.dao.ManageGatewayRouteDao;
import com.dcits.saas.gateway.dao.dataobject.ManageGatewayRouteDO;
import com.dcits.saas.gateway.route.RouteRefreshProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RedisRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 使用Redis动态管理、刷新路由配置
 *
 * @author Klaus
 * @since 2023/10/25
 */
@Service
@RequiredArgsConstructor
public class ManageRouteService {

	private final RedisRouteDefinitionRepository redisRepository;
	private final ManageGatewayRouteDao routeDao;
	private final ReactiveRedisTemplate<String, RouteDefinition> reactiveRedisTemplate;
	private final RouteRefreshProducer routeRefreshProducer;

	public Mono<String> add(ManageGatewayRouteDO routeDO) {
		String nanoId = NanoId.randomNanoId();
		routeDO.setRouteId(nanoId);
		return routeDao.save(routeDO)
				.map(routeDO1 -> Mono.just(routeDO1.toRouteDefinition()))
				.flatMap(redisRepository::save)
				.then(routeRefreshProducer.publishRefresh())
				.thenReturn(nanoId);
	}

	public Mono<Long> deleteById(String routeId) {
		return routeDao.deleteById(routeId)
				.flatMap(count -> {
					if (count == 1) {
						return redisRepository.delete(Mono.just(routeId)).thenReturn(count);
					}
					return Mono.empty();
				})
				.flatMap(count -> {
					if (count == 1) {
						return routeRefreshProducer.publishRefresh();
					}
					return Mono.empty();
				});
	}

	public Flux<RouteDefinition> getRouteDefinitions() {
		return redisRepository.getRouteDefinitions();
	}

	public Mono<Void> initRedis() {
		return deleteAll()
				.thenMany(routeDao.findAllEnabled())
				.map(ManageGatewayRouteDO::toRouteDefinition)
				.flatMap(routeDefinition -> redisRepository.save(Mono.just(routeDefinition)))
				.then(routeRefreshProducer.publishRefresh())
				.then();
	}

	private Mono<Long> deleteAll() {
		return reactiveRedisTemplate.delete(reactiveRedisTemplate.keys("routedefinition_*"));
	}

}
