package com.dhabits.ss.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, String> createToken(String login, String password) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.put("grant_type", List.of("password"));
        body.put("client_id", List.of(clientId));
        body.put("username", List.of(login));
        body.put("password",List.of(password));
        return getStringStringMap(headers, body);

    }
    public Map<String, String> refreshToken(LoggedUser loggedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.put("grant_type", List.of("refresh_token"));
        body.put("client_id", List.of(clientId));
        body.put("refresh_token", List.of(loggedUser.getRefreshToken()));

        return getStringStringMap(headers, body);
    }
    private Map<String, String> getStringStringMap(HttpHeaders headers, MultiValueMap<String, String> body) {
        String url = UriComponentsBuilder.fromHttpUrl(keycloakAuthServerUrl)
                .pathSegment("realms", realm, "protocol", "openid-connect", "token")
                .toUriString();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to refresh token");
        }
    }

}
