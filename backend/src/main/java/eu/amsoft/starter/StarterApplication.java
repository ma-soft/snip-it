package eu.amsoft.starter;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class StarterApplication {

    public static void main(final String[] args) {
        SpringApplication.run(StarterApplication.class, args);
    }

}
