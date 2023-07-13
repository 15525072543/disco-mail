package com.disco.discoGoodsWeb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * @ClassName: GoodsHtmlServiceImpl
 * @Description: 生成html静态模板文件
 * @date: 2023/7/9
 * @author zhb
 */
@Service
public class GoodsHtmlServiceImpl {

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private GoodsServiceImpl goodsService;

    public void createGoodsTemplare(Long spuId){

        //初始化thymeleaf的上下文对象
        Context context = new Context();
        context.setVariables(goodsService.loadData(spuId));

        //初始化输出流
        File file = new File("D:\\App\\nginx-1.14.0\\nginx-1.14.0\\nginx-1.14.0\\html\\item\\" + spuId + ".html");
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
            templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (printWriter != null){
                printWriter.close();
            }
        }
    }
}
