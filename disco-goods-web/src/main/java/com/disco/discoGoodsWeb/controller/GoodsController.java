package com.disco.discoGoodsWeb.controller;

import com.disco.discoGoodsWeb.service.impl.GoodsHtmlServiceImpl;
import com.disco.discoGoodsWeb.service.impl.GoodsServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName: GoodsController
 * @Description: 商品详情页controller
 * @date: 2023/7/7
 * @author zhb
 */
@Controller
public class GoodsController {

    @Resource
    private GoodsServiceImpl goodsService;

    @Resource
    private GoodsHtmlServiceImpl goodsHtmlService;

    @RequestMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id")Long id, Model model){
        Map<String, Object> loadDataMap = this.goodsService.loadData(id);
        model.addAllAttributes(loadDataMap);
        goodsHtmlService.createGoodsTemplare(id);
        return "item";
    }

}
