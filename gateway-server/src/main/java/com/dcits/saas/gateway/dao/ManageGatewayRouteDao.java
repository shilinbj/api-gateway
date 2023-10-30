package com.dcits.saas.gateway.dao;

import com.dcits.saas.gateway.dao.dataobject.ManageGatewayRouteDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Query.query;
import static org.springframework.data.relational.core.query.Criteria.where;

/**
 * @author Klaus
 * @since 2023/10/26
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class ManageGatewayRouteDao {

	private final DatabaseClient client;
	private final R2dbcEntityTemplate template;

	public Flux<ManageGatewayRouteDO> findAllEnabled() {
		// select * from manage_gateway_route where enable_flag = 1
		return template.select(query(where("enable_flag").is(1)), ManageGatewayRouteDO.class);
	}

	public Mono<ManageGatewayRouteDO> findById(String id) {
		// select * from manage_gateway_route where route_id = #{id}
		return template.selectOne(query(where("route_id").is(id)), ManageGatewayRouteDO.class);
	}

	public Mono<ManageGatewayRouteDO> save(ManageGatewayRouteDO routeDO) {
		return template.insert(routeDO);
	}

	public Mono<Long> deleteById(String id) {
		return template.delete(query(where("route_id").is(id)), ManageGatewayRouteDO.class);
	}

}
