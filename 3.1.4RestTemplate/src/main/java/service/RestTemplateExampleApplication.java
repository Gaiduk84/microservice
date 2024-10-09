package service;

import model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestTemplateExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestTemplateExampleApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandLineRunner run(UserService userService) {
        return args -> {
            try {
                userService.performOperations();
                String finalCode = userService.getResult();
                if (finalCode.length() != 18) {
                    System.err.println("Ошибка: итоговый код имеет неправильную длину");
                } else {
                    System.out.println("Итоговый код - " + finalCode);
                }
            } catch (Exception e) {
                System.err.println("Ошибка выполнения операций: " + e.getMessage());
            }
        };
    }
}