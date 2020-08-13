package com.lejia;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.ErrorPageFilter;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.*;

@SpringBootApplication
@ImportResource(value = {"classpath:applicationContext.xml"})
@Configuration
@ComponentScan(basePackages = "com.lejia")
@ServletComponentScan
@EnableAspectJAutoProxy //开启aop切面编程
public class SpringbootApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        System.out.println("====================独立tomcat启动中=====================");
        return application.sources(SpringbootApplication.class);
    }

    public static void main(String[] args) {
        System.out.println("====================内置tomcat启动中=====================");
//    	SpringApplication.run(SpringbootApplication.class, args);
    }

}
