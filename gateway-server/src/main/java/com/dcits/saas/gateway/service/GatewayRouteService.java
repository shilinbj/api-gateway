package com.dcits.saas.gateway.service;

import cn.hutool.core.lang.id.NanoId;
import com.dcits.saas.gateway.dao.GatewayRouteDao;
import com.dcits.saas.gateway.dao.dataobject.RouteDO;
import com.dcits.saas.gateway.dao.repository.GatewayRouteRepository;
import com.dcits.saas.gateway.module.vo.RouteVO;
import com.dcits.saas.gateway.route.RouteRefreshProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.route.RedisRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.dcits.saas.gateway.constants.GatewayConstants.PREFIX_ROUTE_DEFINITION;

/**
 * 使用Redis动态管理、刷新路由配置
 *
 * @author Klaus
 * @since 2023/10/25
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GatewayRouteService {

	private final RedisRouteDefinitionRepository redisRepository;
	private final GatewayRouteRepository routeRepository;
	private final GatewayRouteDao routeDao;
	private final ReactiveRedisTemplate<String, RouteDefinition> reactiveRedisTemplate;
	private final RouteRefreshProducer routeRefreshProducer;

	public Mono<Void> save(RouteVO routeVO) {
		Mono<Long> longMono;
		RouteDO routeDO = new RouteDO(routeVO);
		// if the routevo include route_id then update, else insert
		if (routeVO.getRouteId() == null) {
			longMono = routeRepository.save(routeDO).thenReturn(1L);
		} else {
			longMono = routeDao.update(routeDO);
		}
		return longMono.filter(count -> count > 0)
				.then(redisRepository.save(Mono.just(routeDO.toRouteDefinition())));
	}

	public Mono<Long> updateStatus(Long id, boolean isEnabled) {
		return routeRepository.updateStatus(id, isEnabled);
	}

	public Mono<RouteDO> getById(Long id) {
		return routeRepository.findById(id);
	}

	public Mono<Long> deleteById(Long routeId) {
		return routeRepository.deleteById(routeId)
				.then(redisRepository.delete(Mono.just(routeId.toString())))
				.then(routeRefreshProducer.publishRefresh());
	}

	public Flux<RouteDefinition> getRouteDefinitions() {
		return redisRepository.getRouteDefinitions();
	}

	public Flux<RouteDO> getRoutes(String isEnabled) {
		return routeDao.getRoutes(isEnabled);
	}

	/**
	 * 使用MySQL中数据初始化Redis，会删除Redis中全部路由信息，初始化完成后会通知各节点进行刷新
	 *
	 * @return
	 */
	public Mono<Void> initRedis() {
		return deleteAll()
				.thenMany(routeRepository.findAllEnabled())
				.map(RouteDO::toRouteDefinition)
				.flatMap(routeDefinition -> redisRepository.save(Mono.just(routeDefinition)))
				.then(routeRefreshProducer.publishRefresh())
				.then();
	}

	private Mono<Long> deleteAll() {
		return reactiveRedisTemplate.delete(reactiveRedisTemplate.keys(PREFIX_ROUTE_DEFINITION + "*"));
	}

}
