package com.nta.teabreakorder;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "STORE API", version = "2.0", description = "API DOCS V1"))
@SecurityScheme(name = "securityAPI", scheme = "bearer", type = SecuritySchemeType.HTTP,in = SecuritySchemeIn.HEADER)
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
