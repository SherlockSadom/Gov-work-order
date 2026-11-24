package com.gov.gov_work;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.gov.gov_work.mapper")
@SpringBootApplication
public class GovWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(GovWorkApplication.class, args);
    }

}
