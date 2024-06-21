package com.googleTrendsBigQuery.googleTrendsRestApis.controller;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@RestController
public class LoginController {


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private  String clientId ;


    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private  String clientSecret ;

    @GetMapping("/grantcode")
    public String grantCode(@RequestParam("code") String code) {
        String accessToken = getOauthAccessTokenGoogle(code);
        System.out.println("Access Token in grantCode method: " + accessToken); // Print token to console

        String profileDetails = getProfileDetailsGoogle(accessToken);
        return profileDetails;
    }

    private String getOauthAccessTokenGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
System.out.println(code+"code===========");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("redirect_uri", "http://localhost:8080/grantcode");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("scope", "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email openid");
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);
        System.out.println(requestEntity+"requestEntity===========");
        String url = "https://oauth2.googleapis.com/token";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();

        System.out.println(jsonObject+"jsonObject===========");
        String accessToken = jsonObject.get("access_token").getAsString();
        System.out.println("Access Token: " + accessToken); // Print token to console

        return accessToken;
    }


    private String getProfileDetailsGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();

        String email = jsonObject.get("email").getAsString();
        String name = jsonObject.get("name").getAsString();

        return "User: " + name + " (Email: " + email + ")";
    }
}