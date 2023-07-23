package com.disco.car.contorller;

import com.disco.car.pojo.Cart;
import com.disco.car.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: CarController
 * @Description:
 * @date: 2023/7/22
 * @author zhb
 */
@Controller
public class CarController {

    @Resource
    private CarService carService;

    /**
     * 添加购物车
     * @param cart 购物车对象
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCar(@RequestBody Cart cart){
        this.carService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户查询购物车信息
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCarts(){
        List<Cart> carts = this.carService.queryCarts();
        return ResponseEntity.ok(carts);
    }

    /**
     * 修改商品数量
     * @param cart
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestBody Cart cart){
        this.carService.updateNum(cart);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 删除购物车中的商品
     * @param skuId
     * @return
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") String skuId){
        this.carService.deleteCar(skuId);
        return ResponseEntity.ok().build();
    }
}
