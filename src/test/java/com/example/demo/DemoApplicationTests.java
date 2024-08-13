package com.example.demo;


import com.example.demo.client.RestClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
class DemoApplicationTests {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(DemoApplication.class, args);

        RestClient restClient = context.getBean(RestClient.class);
        restClient.performOperations();
    }
}
