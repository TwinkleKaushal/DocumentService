package com.connection.document.configuration;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * This class is used for details of swagger
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apiDocs() {

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(getInfo()).select().apis(RequestHandlerSelectors.basePackage("com.connection.document"))
                .paths(PathSelectors.any()).build();
    }

    /**
     * This method is used to for describing the details in swagger ui
     * 
     */
    private ApiInfo getInfo() {
        return new ApiInfo("PDF generation Service", "This project is developed by Twinkle", "1.0", "Terms of services",
                new Contact("Twinkle", "https://github.com/TwinkleKaushal", "twinkle.kaushal@indusnet.co.in"), "License of api",
                "api License URL", Collections.emptyList());
    }
}
