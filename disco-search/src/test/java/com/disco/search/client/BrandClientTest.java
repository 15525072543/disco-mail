package com.disco.search.client;

import com.disco.search.SearchServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: BrandClientTest
 * @Description:
 * @date: 2023/3/6
 * @author zhb
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchServiceApplication.class)
public class BrandClientTest{

    @Autowired
    private CategoryClient categoryClient;

    @Test
    public void testQueryCategories(){
        List<String> names = categoryClient.queryNamesByIds(Arrays.asList(1L, 2L, 3L));
        names.forEach(name -> {
            System.out.println(name);
        });
    }

}