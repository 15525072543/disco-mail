package com.disco.service.impl;

import com.disco.service.UploadService;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: UploadServiceImpl
 * @Description: 图片上传服务层实现类
 * @date: 2022/6/1
 * @author zhb
 */
@Service
public class UploadServiceImpl implements UploadService {

    // 图片文件类型集合
    private static final List<String> CONTENT_TYPES = Arrays.asList("image/gif","image/jpeg");

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceImpl.class);

    @Resource
    private FastFileStorageClient storageClient;

    @Override
    public String uploadImage(MultipartFile file) {

        String originalFilename = file.getOriginalFilename();
        try {
            // 1.校验文件类型
            String contentType = file.getContentType();
            if (!CONTENT_TYPES.contains(contentType)){
                LOGGER.info("文件类型不合法:{}",originalFilename );
                return null;
            }
            // 2.校验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null){
                LOGGER.info("文件内容不合法:{}",originalFilename);
                return null;
            }
            // 3.保存到服务器
            String ext = StringUtils.substringAfterLast(originalFilename, ".");
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);
            // 4.返回url, 进行回显
            return "http://image.leyou.com/" + storePath.getFullPath();
        }catch (IOException e) {
            LOGGER.info("服务器内部错误:" + originalFilename);
            e.printStackTrace();
        }
        return null;
    }
}
