package com.ranran;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ranran
 * 启动类
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@MapperScan("com.ranran.persistence.mapper")
@Slf4j
public class RanRanApplication {
    public static void main(String[] args) {
        SpringApplication.run(RanRanApplication.class, args);
        log.info("===== RanRanApplication 启动成功 =====");
    }

}
