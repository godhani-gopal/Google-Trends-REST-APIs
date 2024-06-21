package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.LogInService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class LogInServiceImpl implements LogInService {


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.redirect-uri}")
    private String redirectUri;

    @Value("${google.oauth2.token-url}")
    private String tokenUrl;

    @Value("${google.oauth2.userinfo-url}")
    private String userinfoUrl;

    @Value("${oauth.google.scopes}")
    private String oauthScopes;

    private final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    @Override
    public String getOauthAccessTokenGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("scope", oauthScopes);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, String.class);
        JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();

        String accessToken = jsonObject.get("access_token").getAsString();
        String id_token = jsonObject.get("id_token").getAsString();
        System.out.println(id_token+"id_token=============");
        System.out.println(jsonObject+"jsonObject=============");
        return id_token;
    }
    public GoogleIdToken.Payload verifyToken(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            return idToken.getPayload();
        } else {
            throw new IllegalArgumentException("Invalid ID token.");
        }
    }
    @Override
    public String getProfileDetailsGoogle(String accessToken) {
        try {
            GoogleIdToken.Payload payload = verifyToken(accessToken);
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            return "User: " + name + " (Email: " + email + ")";
        } catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return "Token verification failed: " + e.getMessage();
        }
    }

}
