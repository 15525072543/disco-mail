package com.disco.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: UploadService
 * @Description: 上传服务层接口
 * @date: 2022/6/1
 * @author zhb
 */
public interface UploadService {

    /**
     * 图片上传
     * @param file 图片文件
     * @return 图片存储路径
     */
    String uploadImage(MultipartFile file);
}
