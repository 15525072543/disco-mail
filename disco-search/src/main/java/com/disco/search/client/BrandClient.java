package com.disco.search.client;

import com.disco.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName: BrandClient
 * @Description:
 * @date: 2023/3/6
 * @author zhb
 */
@FeignClient("disco-item-service")
public interface BrandClient extends BrandApi {
}
