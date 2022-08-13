package com.nta.teabreakorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication
//public class StoreManagerApplication extends SpringBootServletInitializer implements CommandLineRunner {
public class StoreManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreManagerApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//
//    }
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(StoreManagerApplication.class);
//    }
}
