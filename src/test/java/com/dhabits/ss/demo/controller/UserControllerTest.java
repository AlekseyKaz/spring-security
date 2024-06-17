package com.dhabits.ss.demo.controller;

import com.dhabits.ss.demo.data.entity.ResourceObjectEntity;
import com.dhabits.ss.demo.data.entity.UserActionsEntity;
import com.dhabits.ss.demo.data.entity.UserEntity;
import com.dhabits.ss.demo.data.repository.ResourceObjectRepository;
import com.dhabits.ss.demo.data.repository.UserActionsRepository;
import com.dhabits.ss.demo.data.repository.UserRepository;
import com.dhabits.ss.demo.domain.dto.ResourceObject;
import com.dhabits.ss.demo.domain.service.ResourceObjectService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import keycloaktestcontainers.KeyCloakTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class ResourceControllerTest extends KeyCloakTestContainers {
    @LocalServerPort
    private int port;

    private String token;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserActionsRepository userActionsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ResourceObjectRepository objectRepository;
    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        token = getPersonBearer();

        UserEntity user = userRepository.findByLogin("admin")
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setLogin("admin");
                    newUser.setOpenPassword("test");
                    newUser.setPassword(passwordEncoder.encode("test"));
                    userRepository.save(newUser);

                    UserActionsEntity actions = new UserActionsEntity();
                    actions.setUser(newUser);
                    actions.setAdmin(true);
                    userActionsRepository.save(actions);

                    return newUser;
                });
        user.getActions().setAdmin(true);
        user.getActions().setRegular(true);
        userRepository.save(user);

        objectRepository.findById(1).orElseGet(() -> {
            ResourceObjectEntity resourceObject = new ResourceObjectEntity(1, "1", "/path");
            return objectRepository.save(resourceObject);
        });
    }

    @Test
    public void testCreateResourceObjectAsAdmin() {
        ResourceObject resourceObject = new ResourceObject();
        resourceObject.setPath("/test/path");
        resourceObject.setId(2);
        resourceObject.setValue("Test Value");

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(resourceObject)
                .when()
                .post("/resource")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("2"));
    }

    @Test
    public void testGetResourceObjectAsRegularUser() {
        ResourceObject resourceObject = new ResourceObject();
        resourceObject.setPath("/test/path");
        resourceObject.setId(2);
        resourceObject.setValue("Test Value");

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(resourceObject)
                .when()
                .post("/resource");

        UserEntity user = userRepository.findByLogin("admin").orElseThrow();
        UserActionsEntity actions = userActionsRepository.findByUser(user).orElseThrow();
        actions.setRegular(true);
        actions.setAdmin(false);
        userActionsRepository.save(actions);

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .when()
                .get("/resource/2")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(2))
                .body("path", equalTo("/test/path"))
                .body("value", equalTo("Test Value"));
    }
}