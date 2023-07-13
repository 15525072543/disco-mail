package com.disco.discoGoodsWeb.client;

import com.disco.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName: CategoryClient
 * @Description:
 * @date: 2023/3/6
 * @author zhb
 */
@FeignClient("disco-item-service")
public interface CategoryClient extends CategoryApi {
}
