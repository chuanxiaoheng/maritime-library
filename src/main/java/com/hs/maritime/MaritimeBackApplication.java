package com.hs.maritime;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hs.maritime.mapper")
public class MaritimeBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaritimeBackApplication.class, args);
    }

}
