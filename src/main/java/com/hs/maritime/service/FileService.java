package com.hs.maritime.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件业务接口
 * */
public interface FileService {

    /**
     * 上传文件
     * */
    String upload(MultipartFile file,String filePath) throws IOException;
    /**
     * 下载文件
     * */
    void download(String fileName, String filePath, HttpServletResponse response) throws IOException;
}
