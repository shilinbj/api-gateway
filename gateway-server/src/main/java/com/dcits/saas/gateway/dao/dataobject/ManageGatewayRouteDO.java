package com.dcits.saas.gateway.dao.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Klaus
 * @since 2023/10/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("manage_gateway_route")
public class ManageGatewayRouteDO {

	/**
	 * 路由Code
	 */
	@Id
	private String routeId;

	/**
	 * 路由名称
	 */
	private String routeName;

	/**
	 * 路由URI
	 */
	private String routeUri;

	/**
	 * 路由分发地址","分隔
	 */
	private String routePaths;

	/**
	 * 路由排序
	 */
	private Integer routeSort;

	/**
	 * 限流策略code
	 */
	private String limitStrategyCode;

	/**
	 * 限流策略
	 */
	private String limitStrategy;

	/**
	 * 每秒并发量
	 */
	private Integer limitReplenishRate;

	/**
	 * 最大并发量
	 */
	private Integer limitBurstCapacity;

	/**
	 * 是否启用：1-启用 0-禁用
	 */
	private Integer enableFlag;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 创建人ID
	 */
	private String creatorId;

	/**
	 * 创建人名称
	 */
	private String creatorName;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

	public RouteDefinition toRouteDefinition() {
		RouteDefinition routeDefinition = new RouteDefinition();
		routeDefinition.setId(this.getRouteId());
		URI routeUri = UriComponentsBuilder.fromUriString(this.getRouteUri()).build().toUri();
		routeDefinition.setUri(routeUri);
		routeDefinition.setOrder(this.getRouteSort());
		List<PredicateDefinition> predicates = getPredicates(this.getRoutePaths());
		if (!predicates.isEmpty()) {
			routeDefinition.setPredicates(predicates);
		}
		// 获取限流过滤器
		List<FilterDefinition> filters = new ArrayList<>();
		if (!ObjectUtils.isEmpty(this.getLimitStrategy())) {
			Map<String, String> args = new LinkedHashMap<>();
			args.put("key-resolver", this.getLimitStrategyCode());
			args.put("redis-rate-limiter.replenishRate", String.valueOf(this.getLimitReplenishRate()));
			args.put("redis-rate-limiter.burstCapacity", String.valueOf(this.getLimitBurstCapacity()));
			FilterDefinition filterDefinition = new FilterDefinition();
			filterDefinition.setName("RequestRateLimiter");
			filterDefinition.setArgs(args);
			filters.add(filterDefinition);
		}
		routeDefinition.setFilters(filters);
		return routeDefinition;
	}

	/**
	 * 获取地址断言
	 *
	 * @param routePaths 路由地址
	 * @return java.util.List
	 */
	private List<PredicateDefinition> getPredicates(String routePaths) {
		Map<String, String> args = new LinkedHashMap<>();
		String[] paths = routePaths.split(",");
		for (int i = 0; i < paths.length; i++) {
			args.put("arg_" + i, paths[i]);
		}
		PredicateDefinition predicate = new PredicateDefinition();
		predicate.setName("Path");
		predicate.setArgs(args);

		List<PredicateDefinition> predicates = new ArrayList<>();
		predicates.add(predicate);
		return predicates;
	}

}
