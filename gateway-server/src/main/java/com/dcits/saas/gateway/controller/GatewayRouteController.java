package com.dcits.saas.gateway.controller;

import com.dcits.saas.gateway.dao.dataobject.RouteDO;
import com.dcits.saas.gateway.module.vo.RouteVO;
import com.dcits.saas.gateway.route.RouteRefreshProducer;
import com.dcits.saas.gateway.service.GatewayRouteService;
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
public class GatewayRouteController {

	private final GatewayRouteService gatewayRouteService;
	private final RouteRefreshProducer routeRefreshProducer;

	/**
	 * 获取Redis中全部的RouteDefinition对象
	 *
	 * @return
	 */
	@GetMapping("getRouteDefinition")
	public Flux<RouteDefinition> getRouteDefinition() {
		return gatewayRouteService.getRouteDefinitions();
	}

	/**
	 * 获取MySQL中路由配置信息
	 * isEnabled: Empty -> All, 1 -> Enabled, 0 -> Disabled
	 *
	 * @param isEnabled
	 * @return
	 */
	@GetMapping("getRoutes")
	public Flux<RouteVO> getRoutes(@RequestParam(value = "isEnabled", required = false) String isEnabled) {
		return gatewayRouteService.getRoutes(isEnabled).map(RouteDO::toRouteVo);
	}

	@GetMapping("getById/{id}")
	public Mono<RouteVO> getById(@PathVariable Long id) {
		return gatewayRouteService.getById(id).map(RouteDO::toRouteVo);
	}

	/**
	 * 添加路由配置
	 *
	 * @param routeVO
	 * @return route_id
	 */
	@PostMapping("save")
	public Mono<Void> save(@RequestBody RouteVO routeVO) {
		return gatewayRouteService.save(routeVO);
	}

	/**
	 * 根据route_id删除路由配置
	 *
	 * @param routeId
	 * @return
	 */
	@PostMapping("delete/{routeId}")
	public Mono<String> delete(@PathVariable Long routeId) {
		return gatewayRouteService.deleteById(routeId).thenReturn("success");
	}

	/**
	 * 初始化Redis
	 *
	 * @return
	 */
	@PostMapping("initRedis")
	public Mono<String> initRedis() {
		return gatewayRouteService.initRedis().thenReturn("success");
	}

	/**
	 * 启用路由
	 *
	 * @param id
	 * @return
	 */
	@PostMapping("enable/{id}")
	public Mono<Long> enable(@PathVariable Long id) {
		return gatewayRouteService.updateStatus(id, true);
	}

	/**
	 * 停用路由
	 *
	 * @param id
	 * @return
	 */
	@PostMapping("disable/{id}")
	public Mono<Long> disable(@PathVariable Long id) {
		return gatewayRouteService.updateStatus(id, false);
	}

//	@GetMapping("publish")
//	public Mono<Long> publish() {
//		return routeRefreshProducer.publishRefresh();
//	}

}
