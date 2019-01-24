package com.abcxo.ifootball.service;

/**
 * Created by shadow on 15/10/23.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application{

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}

//@SpringBootApplication
//@EnableScheduling
//public class Application extends SpringBootServletInitializer {
//
//    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
//    }
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(Application.class);
//    }
//}
