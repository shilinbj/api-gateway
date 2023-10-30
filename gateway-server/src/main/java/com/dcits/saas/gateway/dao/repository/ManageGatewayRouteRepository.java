//package com.dcits.saas.gateway.dao.repository;
//
//import com.dcits.saas.gateway.dao.dataobject.ManageGatewayRouteDO;
//import org.springframework.data.r2dbc.repository.Query;
//import org.springframework.data.r2dbc.repository.R2dbcRepository;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
///**
// * @author Klaus
// * @since 2023/10/26
// */
//@Repository
//public interface ManageGatewayRouteRepository extends R2dbcRepository<ManageGatewayRouteDO, String> {
//
//	@Query("select * from manage_gateway_route where enable_flag = 1")
//	Flux<ManageGatewayRouteDO> findAllEnabled();
//
//}
