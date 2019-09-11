package com.Nintendo.controller;

import com.Nintendo.file.FastDFSFile;
import com.Nintendo.util.FastDFSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Package: com.Nintendo.controller
 * @Author: ZZM
 * @Date: Created in 2019/8/19 11:34
 * @Address:CN.SZ
 **/
@RestController
@CrossOrigin
//跨域访问(域名,协议,端口其中一个不一致时)
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @PostMapping("/upload")
    public String[] upload(MultipartFile file) {
        try {
            //获取原来的文件名,文件本身字节数组,文件的扩展名例如:jpg
            FastDFSFile fastDFSFile = new FastDFSFile(file.getOriginalFilename(), file.getBytes(), StringUtils.getFilenameExtension(file.getOriginalFilename()));
            String[] upload = FastDFSClient.upload(fastDFSFile);
            return upload;
        } catch (Exception e) {
            log.error("文件上传发生异常", e);
        }
        return null;
    }
}
