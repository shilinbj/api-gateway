package com.dcits.saas.gateway.controller;

import com.dcits.saas.gateway.dao.dataobject.ManageGatewayRouteDO;
import com.dcits.saas.gateway.route.RouteRefreshProducer;
import com.dcits.saas.gateway.service.ManageRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 路由配置管理接口
 *
 * @author Klaus
 * @since 2023/10/25
 */
@RestController
@RequestMapping("manage_route")
@RequiredArgsConstructor
public class ManageRouteController {

	private final ManageRouteService manageRouteService;
	private final RouteRefreshProducer routeRefreshProducer;

	/**
	 * 获取当前生效的全部路由配置
	 *
	 * @return
	 */
	@GetMapping("getRoutes")
	public Flux<RouteDefinition> getRoutes() {
		return manageRouteService.getRouteDefinitions();
	}

	/**
	 * 添加路由配置
	 *
	 * @param routeDO
	 * @return route_id
	 */
	@PostMapping("add")
	public Mono<String> add(@RequestBody ManageGatewayRouteDO routeDO) {
		return manageRouteService.add(routeDO);
	}

	/**
	 * 根据route_id删除路由配置
	 *
	 * @param routeId
	 * @return
	 */
	@PostMapping("delete/{routeId}")
	public Mono<String> delete(@PathVariable String routeId) {
		return manageRouteService.deleteById(routeId).thenReturn("success");
	}

	/**
	 * 初始化Redis
	 *
	 * @return
	 */
	@PostMapping("initRedis")
	public Mono<String> initRedis() {
		return manageRouteService.initRedis().thenReturn("success");
	}

	@GetMapping("publish")
	public Mono<Long> publish() {
		return routeRefreshProducer.publishRefresh();
	}

}
