package com.disco.car.feign;

import com.disco.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName: GoodsClient
 * @Description:
 * @date: 2023/7/22
 * @author zhb
 */
@FeignClient("disco-item-service")
public interface GoodsClient extends GoodsApi {
}
