package com.gfs.cpp.web.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile("swagger")
@Configuration
@EnableSwagger2
public class SwaggerConfig {


    String securityRealm = "GordonLine";
    SecurityScheme basicAuthSecurityScheme = new BasicAuth(securityRealm);
    SecurityContext securityContext = SecurityContext.builder().securityReferences(Arrays.asList(new SecurityReference(securityRealm, new AuthorizationScope[0]))).build();

    
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(Arrays.asList(basicAuthSecurityScheme))
            .securityContexts(Arrays.asList(securityContext))
            .apiInfo(apiInfo());
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Contract Price Profile")
            .description("Contract Price Profile APIs for creating contract in cpp and activating pricing for customer.")
            .version("0.0.1-SNAPSHOT")
            .termsOfServiceUrl("http://www.gfs.com")
            .build();
    }

}