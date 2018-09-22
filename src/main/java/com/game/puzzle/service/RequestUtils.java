package com.game.puzzle.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


/**
 * Created by maninder on 29/11/2017.
 */
@Component
public class RequestUtils {

    public JsonNode getData(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", System.getenv("token"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JsonNode> entity = new HttpEntity<JsonNode>(null, headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class).getBody();
    }

    public String postTocken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", System.getenv("authToken"));
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        JsonNode body = restTemplate.postForEntity("https://api.twitter.com/oauth2/token", request, JsonNode.class).getBody();
        String token = body.get("token_type").asText() + " " + body.get("access_token").asText();
        return token;

    }

}
