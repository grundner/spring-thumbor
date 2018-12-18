package biz.grundner.springframework.thumbor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Example: http://localhost:8080/isabell.jpg?thumbor&size=600x
 *
 * @author Stephan Grundner
 */
@SpringBootApplication
@Import({ThumborConfiguration.class, Demo.Config.class})
public class Demo {

    public static void main(String[] args) {
        SpringApplication.run(Demo.class, args);
    }

    public static class Config implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/**").addResourceLocations("file:images/");
        }
    }
}
