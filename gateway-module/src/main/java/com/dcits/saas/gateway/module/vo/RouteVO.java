package com.dcits.saas.gateway.module.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 路由配置信息
 *
 * @author Klaus
 * @since 2023/10/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteVO {

	/**
	 * Route ID
	 */
	private Long routeId;

	/**
	 * Route name
	 */
	private String routeName;

	/**
	 * Route uri
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
	 * 限流策略code, spring bean name
	 * example @Bean("ipKeyResolver") limitStrategyCode = #{@ipKeyResolver}
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime updateTime;

	/**
	 * 更新人ID
	 */
	private String updaterId;

	/**
	 * 更新人名称
	 */
	private String updaterName;

}
