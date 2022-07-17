package com.disco.controller;

import com.disco.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @ClassName: UploadController
 * @Description: 文件上传视图层
 * @date: 2022/6/1
 * @author zhb
 */
@Controller
@RequestMapping("upload")
public class UploadController {

    @Resource
    private UploadService uploadService;

    @RequestMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file){
        String url = uploadService.uploadImage(file);
        if (StringUtils.isEmpty(url)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(url);
    }
}
