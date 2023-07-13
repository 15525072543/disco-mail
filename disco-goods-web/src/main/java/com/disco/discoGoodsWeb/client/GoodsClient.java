package com.disco.discoGoodsWeb.client;

import com.disco.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName: Goods
 * @Description:
 * @date: 2023/3/6
 * @author zhb
 */
@FeignClient("disco-item-service")
public interface GoodsClient extends GoodsApi {
}
