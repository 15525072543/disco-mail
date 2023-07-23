package com.disco.car.service;

import com.disco.api.GoodsApi;
import com.disco.auth.common.pojo.UserInfo;
import com.disco.car.interceptor.LoginInterceptor;
import com.disco.car.pojo.Cart;
import com.disco.pojo.Sku;
import com.leyou.common.utils.JsonUtils;
import com.sun.javaws.LocalInstallHandler;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: CarService
 * @Description:
 * @date: 2023/7/22
 * @author zhb
 */
@Service
public class CarService {

    private static final String KEY_PREFIX = "user:cart:";

    @Resource
    private StringRedisTemplate template;

    @Resource
    private GoodsApi goodsApi;

    /**
     * 添加购物车
     * @param cart
     */
    public void addCart(Cart cart) {
        //获取用户信息
        UserInfo user = LoginInterceptor.getUserInfo();
        //判断redis中，该商品是否存在
        //BoundHashOperations<String, Object, Object>  String: KEY_PREFIX + user.getId() Object：skuId Object：cart
        BoundHashOperations<String, Object, Object> hashOperations = this.template.boundHashOps(KEY_PREFIX + user.getId());
        String skuId = cart.getSkuId().toString();
        Integer num = cart.getNum();
        if (hashOperations.hasKey(skuId)){
            //存在，更新数量
            String carJson = hashOperations.get(skuId).toString();
            cart = JsonUtils.parse(carJson, Cart.class);
            cart.setNum(cart.getNum() + num);
        }else {
            //不存在，添加购物车
            Sku sku = goodsApi.querySkuBySkuId(cart.getSkuId());
            cart.setNum(num);
            cart.setImage(sku.getImages() == null ? "" : sku.getImages().split(",")[0]);
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setUserId(user.getId());
        }
        hashOperations.put(skuId,JsonUtils.serialize(cart));
    }

    public List<Cart> queryCarts() {
        //获取用户信息
        UserInfo user = LoginInterceptor.getUserInfo();
        //如果该用户没有购物车信息，返回null
        if (!this.template.hasKey(KEY_PREFIX + user.getId())){
            return null;
        }
        //根据用户查询购物车信息
        BoundHashOperations<String, Object, Object> boundHashOps = this.template.boundHashOps(KEY_PREFIX + user.getId());
        List<Object> carts = boundHashOps.values();
        //如果该用户的购物车中没有商品，返回null
        if (CollectionUtils.isEmpty(carts)){
            return null;
        }
        //将查询出来的json字符串反序列化
        return carts.stream().map(cart ->
            JsonUtils.parse(cart.toString(), Cart.class)
        ).collect(Collectors.toList());
    }

    public void updateNum(Cart cart) {
        //获取用户信息
        UserInfo user = LoginInterceptor.getUserInfo();
        //获取该用户下的商品
        if (!this.template.hasKey(KEY_PREFIX + user.getId())){
            return;
        }
        BoundHashOperations<String, Object, Object> boundHashOps = this.template.boundHashOps(KEY_PREFIX + user.getId());
        String skuId = cart.getSkuId().toString();
        Integer num = cart.getNum();
        String cartJson = boundHashOps.get(skuId).toString();
        cart = JsonUtils.parse(cartJson, Cart.class);
        //设置商品数量
        cart.setNum(num);
        //写入redis中
        boundHashOps.put(skuId, JsonUtils.serialize(cart));
    }

    public void deleteCar(String skuId) {
        //获取用户信息
        UserInfo user = LoginInterceptor.getUserInfo();
        //从redis中获取Map
        BoundHashOperations<String, Object, Object> boundHashOps = this.template.boundHashOps(KEY_PREFIX + user.getId());
        //根据skuId（key）删除Map
        boundHashOps.delete(skuId);
    }
}
