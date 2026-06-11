package com.hs.maritime.controller;

import com.hs.maritime.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class WebController {

    @Value("${file.avatar-dir}")
    private String avatarDir;

    @Value("${file.cover-dir}")
    private String coverDir;

    @Resource
    private FileService fileService;

    /**
     * 下载头像
     * */
    @GetMapping("/download/avatar/{fileName}")
    public void downloadAvatar(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        fileService.download(fileName,avatarDir,response);

    }
    /**
     * 下载图书封面
     * */
    @GetMapping("/download/cover/{fileName}")
    public void downloadCover(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        fileService.download(fileName,coverDir,response);

    }
}
