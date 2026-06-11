package com.hs.maritime.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import com.hs.maritime.exceptions.MaritimeException;
import com.hs.maritime.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String upload(MultipartFile file, String filePath) throws IOException {
        // 上传文件夹不存在，则创建
        if(!FileUtil.isDirectory(filePath)){
            FileUtil.mkdir(filePath);
        }
        // 获取原文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 生成新文件名
        String fileName = RandomUtil.randomString(16)+ extension;
        // 上传头像
        FileUtil.writeBytes(file.getBytes(),filePath+ fileName);

        // 返回新文件名称
        return fileName;
    }

    @Override
    public void download(String fileName, String filePath, HttpServletResponse response) throws IOException {
        // 下载文件完整路径
        String fileNamePath = filePath + "/" + fileName;
        if(!FileUtil.exist(filePath)){
            throw new MaritimeException("文件不存在！");
        }
        // 读取文件
        byte[] bytes = FileUtil.readBytes(fileNamePath);
        ServletOutputStream out = response.getOutputStream();

        // 文件输出到客户端
        out.write(bytes);
        out.flush();
        out.close();
    }
}
