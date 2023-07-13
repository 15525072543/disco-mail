package com.disco.search.lisenter;

import com.disco.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @ClassName: GoodsListener
 * @Description: rabbitMQ 监听器
 * @date: 2023/7/12
 * @author zhb
 */
@Component
public class GoodsListener {

    @Resource
    private SearchService searchService;

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "LEYOU.SEARCH.SAVE.QUEUE",durable = "true"),
        exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
        key = {"item.insert","item.update"}
    ))
    public void save(Long id) throws IOException {
        searchService.saveGoods(id);
    }
}
