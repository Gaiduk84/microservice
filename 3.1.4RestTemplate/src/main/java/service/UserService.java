package service;


import model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;



@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final String url = "http://94.198.50.185:7081/api/users";
    private final RestTemplate restTemplate;
    private final HttpHeaders headers = new HttpHeaders();
    private final StringBuilder result = new StringBuilder();

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void performOperations() {
        String sessionId = getAllUsers();
        headers.set("cookie", sessionId);
        createUser();
        updateUser();
        deleteUser(3L);
    }

    private String getAllUsers() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return String.join(";", Objects.requireNonNull(response.getHeaders().get("set-cookie")));
        } catch (Exception e) {
            logger.error("Ошибка получения всех пользователей: ", e);
            throw e;
        }
    }

    private void createUser() {
        try {
            User user = new User(3L, "James", "Brown", (byte) 30, "james.brown@example.com");
            HttpEntity<User> entity = new HttpEntity<>(user, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            result.append(response.getBody());
        } catch (Exception e) {
            logger.error("Ошибка создания пользователя: ", e);
            throw e;
        }
    }

    private void updateUser() {
        try {
            User user = new User(3L, "Thomas", "Shelby", (byte) 30, "thomas.shelby@example.com");
            HttpEntity<User> entity = new HttpEntity<>(user, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            result.append(response.getBody());
        } catch (Exception e) {
            logger.error("Ошибка обновления пользователя: ", e);
            throw e;
        }
    }

    private void deleteUser(Long id) {
        try {
            HttpEntity<User> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url + "/" + id, HttpMethod.DELETE, entity, String.class);
            result.append(response.getBody());
        } catch (Exception e) {
            logger.error("Ошибка удаления пользователя: ", e);
            throw e;
        }
    }

    public String getResult() {
        return result.toString();
    }
}