package com.dcits.saas.gateway.dao.repository;

import com.dcits.saas.gateway.dao.dataobject.RouteDO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Klaus
 * @since 2023/10/26
 */
@Repository
public interface GatewayRouteRepository extends R2dbcRepository<RouteDO, Long> {

    // Wrong column name will lead to empty results.
    @Query("select * from gateway_route where is_enabled = 1")
    Flux<RouteDO> findAllEnabled();

	@Query("update gateway_route set is_enabled = :isEnabled where route_id = :id")
	Mono<Long> updateStatus(Long id, boolean isEnabled);

//	@Query("select * from gateway_route where route_id = :id")
//	Mono<RouteDO> findById(Long id);

}
