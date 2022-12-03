package eu.amsoft.snipit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
public class SpringfoxConfig {
    private static final String BASE_RESSOURCES_PACKAGE = "eu.amsoft.snipit.ressources";

    @Bean
    public Docket api() {
        return new Docket(SWAGGER_2)
                .select()
                .apis(basePackage(BASE_RESSOURCES_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }
}