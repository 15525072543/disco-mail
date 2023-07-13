package com.disco.discoGoodsWeb.listener;

import com.disco.discoGoodsWeb.service.impl.GoodsHtmlServiceImpl;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName: GoodsListener
 * @Description: rabbitMQ 监听器
 * @date: 2023/7/12
 * @author zhb
 */
@Component
public class GoodsListener {

    @Resource
    private GoodsHtmlServiceImpl goodsHtmlService;

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "LEYOU.ITEM.SAVE.QUEUE",durable = "true"),
        exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
        key = {"item.insert","item.update"}
    ))
    public void save(Long id){
        if (id == null){
            return;
        }
        this.goodsHtmlService.createGoodsTemplare(id);
    }
}
