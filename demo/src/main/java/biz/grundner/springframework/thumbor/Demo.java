package biz.grundner.springframework.thumbor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Example: http://localhost:8080/isabell.jpg?thumbor&size=600x
 *
 * @author Stephan Grundner
 */
@SpringBootApplication
@Import(ThumborConfiguration.class)
public class Demo {

    public static void main(String[] args) {
        SpringApplication.run(Demo.class, args);
    }
}
