package com.artax.gateway.service;

import com.artax.gateway.config.PropertiesConfig;
import com.artax.gateway.dto.TokenRequest;
import com.artax.gateway.exception.CustomizedREExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthenticateService {

@Autowired
PropertiesConfig propertiesConfig;

    public  String getToken(TokenRequest tokenRequest){


        // Set up Keycloak token endpoint URL and client credentials
        String keycloakTokenUrl = propertiesConfig.getTokenUrl();
        String clientId = propertiesConfig.getClientid();
        String clientSecret = propertiesConfig.getClientSecret();

        System.out.println("ClientID:"+clientId);
        System.out.println("URL:"+keycloakTokenUrl);
        System.out.println("Secret:"+clientSecret);
        String username = tokenRequest.username();
        String password = tokenRequest.password();
        // Create a RestTemplate for making HTTP requests

        System.out.println("username"+username);


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomizedREExceptionHandler());

        // Set up the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Set up the request body with the necessary parameters
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "password");
        requestBody.add("client_id", clientId);
      requestBody.add("client_secret", clientSecret);
        requestBody.add("username", username);
        requestBody.add("password", password);

        // Create the HTTP request entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make the request to Keycloak token endpoint
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(keycloakTokenUrl, requestEntity, Map.class);

        System.out.println("respone Entity"+responseEntity.getBody());
        // Extract the access token from the response
        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            return responseEntity.getBody().get("access_token").toString();
        }
        else if (responseEntity.getStatusCode().is5xxServerError()) {
            //Handle SERVER_ERROR
            throw new RuntimeException("Wrong Credentials");
        }else {
            throw new RuntimeException("Error obtaining access token from Keycloak");
        }
    }
}
