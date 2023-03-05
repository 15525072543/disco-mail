package com.disco.search.client;

import com.disco.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName: SpecificationClient
 * @Description:
 * @date: 2023/3/6
 * @author zhb
 */
@FeignClient("disco-item-service")
public interface SpecificationClient extends SpecificationApi {
}
