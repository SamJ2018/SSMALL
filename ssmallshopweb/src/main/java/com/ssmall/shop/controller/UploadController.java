package com.ssmall.shop.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import utils.FastDFSClient;

@RestController
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    @RequestMapping("/upload")
    public Result upload(MultipartFile file) {//上传的文件

        String originalFilename = file.getOriginalFilename(); //获取文件全名称
        String exName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);//得到文件拓展名

        try {
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            String fileId = client.uploadFile(file.getBytes(), exName);
            String url = FILE_SERVER_URL + fileId;//图片的完整路径
            return new Result(true, url);//返回给前端
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败");
        }
    }
}
