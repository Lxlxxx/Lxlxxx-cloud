package com.example.auth;

import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LxlxxxAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(LxlxxxAuthApplication.class, args);
        LoggerFactory.getLogger(LxlxxxAuthApplication.class).info(
                "《《《《《《 ROBOT started up successfully at {} {} 》》》》》》", LocalDate.now(), LocalTime.now());
        System.out.println("(♥◠‿◠)ﾉﾞ  权限认证模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" );
    }



}
