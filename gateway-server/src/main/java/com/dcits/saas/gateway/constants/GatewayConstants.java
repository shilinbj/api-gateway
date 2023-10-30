package com.dcits.saas.gateway.constants;

/**
 * @author Klaus
 * @since 2023/10/27
 */
public class GatewayConstants {

	public static final String TOPIC_REFRESH_ROUTES = "refresh_routes_";

	/**
	 * Prefix key of route definition in redis.
	 * Don't change the value.
	 * org.springframework.cloud.gateway.route.RedisRouteDefinitionRepository#ROUTEDEFINITION_REDIS_KEY_PREFIX_QUERY
	 */
	public static final String PREFIX_ROUTE_DEFINITION = "routedefinition_";

}
