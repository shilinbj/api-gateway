package com.dcits.saas.gateway.dao;

import com.dcits.saas.gateway.dao.dataobject.RouteDO;
import com.dcits.saas.gateway.dao.repository.GatewayRouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

/**
 * Gateway routes dao by mysql r2dbc.
 *
 * @author Klaus
 * @since 2023/10/26
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class GatewayRouteDao {

	private final DatabaseClient client;
	private final R2dbcEntityTemplate template;

	public Flux<RouteDO> getRoutes(String isEnabled) {
		Query query;
		if (StringUtils.isEmpty(isEnabled)) {
			query = Query.empty();
		} else {
			query = query(where("is_enabled").is(isEnabled));
		}
		return template.select(query, RouteDO.class);
	}

	public Mono<Long> update(RouteDO routeDO) {
		return template.update(
				query(where("route_id").is(routeDO.getRouteId())),
				Update.update("route_name", routeDO.getRouteName())
						.set("route_uri", routeDO.getRouteUri())
						.set("route_paths", routeDO.getRoutePaths())
						.set("route_sort", routeDO.getRouteSort())
						.set("limit_strategy_code", routeDO.getLimitStrategyCode())
						.set("limit_strategy", routeDO.getLimitStrategy())
						.set("limit_replenish_rate", routeDO.getLimitReplenishRate())
						.set("limit_burst_capacity", routeDO.getLimitBurstCapacity())
						.set("is_enabled", routeDO.isEnabled()),
				RouteDO.class);
	}

}
