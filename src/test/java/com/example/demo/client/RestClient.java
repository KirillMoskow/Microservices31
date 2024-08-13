package com.example.demo.client;

import com.example.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Component
public class RestClient {

    private static final String BASE_URL = "http://94.198.50.185:7081/api/users";
    private String sessionId;

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    public RestClient() {
        this.restTemplate = new RestTemplate();
    }

    public void performOperations() {
        try {
            StringBuilder finalCode = new StringBuilder();

            // Получить список всех пользователей
            logger.info("Запрос списка всех пользователей...");
            ResponseEntity<User[]> response = restTemplate.getForEntity(BASE_URL, User[].class); // GET запрос для получения списка пользователей
            if (response.getStatusCode() == HttpStatus.OK) {
                List<User> users = Arrays.asList(response.getBody());
                logger.info("Список пользователей получен: " + users);
            } else {
                logger.error("Ошибка при получении списка пользователей: " + response.getStatusCode());
                return;
            }

            // Сохранить session id
            sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            logger.info("Сохранен идентификатор сессии: " + sessionId);

            // Создание пользователя
            User newUser = new User();
            newUser.setId(3L);
            newUser.setName("James");
            newUser.setLastName("Brown");
            newUser.setAge(25);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Cookie", sessionId);

            HttpEntity<User> createRequest = new HttpEntity<>(newUser, headers);
            logger.info("Отправка запроса на создание пользователя...");
            ResponseEntity<String> createResponse = restTemplate.postForEntity(BASE_URL, createRequest, String.class);
            if (createResponse.getStatusCode() == HttpStatus.OK) {
                String createCode = createResponse.getBody();
                finalCode.append(createCode);
                logger.info("Пользователь успешно создан. Ответ сервера (часть кода): " + createCode);
            } else {
                logger.error("Ошибка при создании пользователя: " + createResponse.getStatusCode());
                return;
            }

            // Изменение пользователя
            newUser.setName("Thomas");
            newUser.setLastName("Shelby");

            HttpEntity<User> updateRequest = new HttpEntity<>(newUser, headers);
            logger.info("Отправка запроса на изменение пользователя...");
            ResponseEntity<String> updateResponse = restTemplate.exchange(BASE_URL, HttpMethod.PUT, updateRequest, String.class);
            if (updateResponse.getStatusCode() == HttpStatus.OK) {
                String updateCode = updateResponse.getBody();
                finalCode.append(updateCode);
                logger.info("Пользователь успешно изменен. Ответ сервера (часть кода): " + updateCode);
            } else {
                logger.error("Ошибка при изменении пользователя: " + updateResponse.getStatusCode());
                return;
            }

            // Удаление пользователя
            HttpHeaders deleteHeaders = new HttpHeaders();
            deleteHeaders.set("Cookie", sessionId);

            HttpEntity<Void> deleteRequest = new HttpEntity<>(deleteHeaders);
            logger.info("Отправка запроса на удаление пользователя...");
            ResponseEntity<String> deleteResponse = restTemplate.exchange(BASE_URL + "/3", HttpMethod.DELETE, deleteRequest, String.class);
            if (deleteResponse.getStatusCode() == HttpStatus.OK) {
                String deleteCode = deleteResponse.getBody();
                finalCode.append(deleteCode);
                logger.info("Пользователь успешно удален. Ответ сервера (часть кода): " + deleteCode);
            } else {
                logger.error("Ошибка при удалении пользователя: " + deleteResponse.getStatusCode());
            }

            logger.info("Итоговый код: " + finalCode);
        } catch (Exception e) {
            logger.error("Ошибка при выполнении операций: ", e);
        }
    }
}
