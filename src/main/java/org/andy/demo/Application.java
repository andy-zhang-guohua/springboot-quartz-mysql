package org.andy.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("org.andy.demo")
@MapperScan("org.andy.demo.dao")
@SpringBootApplication
public class
Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
