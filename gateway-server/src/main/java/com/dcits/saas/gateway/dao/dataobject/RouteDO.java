package com.dcits.saas.gateway.dao.dataobject;

import com.dcits.saas.gateway.module.vo.RouteVO;
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
@Table("gateway_route")
public class RouteDO {

	/**
	 * 路由Id
	 */
	@Id
	private Long routeId;

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
	 * 是否启用
	 */
	private boolean isEnabled;

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

	/**
	 * 更新人ID
	 */
	private String updaterId;

	/**
	 * 更新人名称
	 */
	private String updaterName;

	public RouteDO(RouteVO routeVO) {
		this.routeId = routeVO.getRouteId();
		this.routeName = routeVO.getRouteName();
		this.routeUri = routeVO.getRouteUri();
		this.routePaths = routeVO.getRoutePaths();
		this.routeSort = routeVO.getRouteSort();
		this.limitStrategyCode = routeVO.getLimitStrategyCode();
		this.limitStrategy = routeVO.getLimitStrategy();
		this.limitReplenishRate = routeVO.getLimitReplenishRate();
		this.limitBurstCapacity = routeVO.getLimitBurstCapacity();
		this.isEnabled = routeVO.isEnabled();
	}

	public RouteVO toRouteVo() {
		return RouteVO.builder()
				.routeId(this.getRouteId())
				.routeName(this.routeName)
				.routeUri(this.routeUri)
				.routePaths(this.routePaths)
				.routeSort(this.routeSort)
				.limitStrategyCode(this.limitStrategyCode)
				.limitStrategy(this.limitStrategy)
				.limitReplenishRate(this.limitReplenishRate)
				.limitBurstCapacity(this.limitBurstCapacity)
				.isEnabled(this.isEnabled)
				.createTime(this.createTime)
				.creatorId(this.creatorId)
				.creatorName(this.creatorName)
				.updateTime(this.updateTime)
				.updaterId(this.updaterId)
				.updaterName(this.updaterName)
				.build();
	}

	public RouteDefinition toRouteDefinition() {
		RouteDefinition routeDefinition = new RouteDefinition();
		routeDefinition.setId(this.getRouteId().toString());
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
