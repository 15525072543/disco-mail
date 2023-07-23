package com.disco.auth.service.client;

import com.disco.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName: UserClient
 * @Description:
 * @date: 2023/7/15
 * @author zhb
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
