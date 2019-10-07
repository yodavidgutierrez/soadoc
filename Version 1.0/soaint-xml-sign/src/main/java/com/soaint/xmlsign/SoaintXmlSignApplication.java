package com.soaint.xmlsign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.soaint")
@SpringBootApplication
public class SoaintXmlSignApplication extends SpringBootServletInitializer {

    @Override
    public SpringApplicationBuilder configure (SpringApplicationBuilder app){
        return app.sources(SoaintXmlSignApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SoaintXmlSignApplication.class, args);
    }

}
