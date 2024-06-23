package com.googleTrendsBigQuery.googleTrendsRestApis.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class GoogleJwtProvider {

    private final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

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

    @Value("{$jwt.admin.emails}")
    private String adminEmails;

    public GoogleJwtProvider(GoogleIdTokenVerifier googleIdTokenVerifier) {
        this.googleIdTokenVerifier = googleIdTokenVerifier;
    }

    private List<String> getAdminEmails() {
        return Arrays.asList(adminEmails.split(","));
    }

    public String getOauthIdTokenGoogle(String code) {
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
        JsonObject jsonObject = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();

        return jsonObject.get("id_token").getAsString();
    }

    public GoogleIdToken.Payload getJwtPayload(String idTokenString) throws GeneralSecurityException, IOException {

        GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString);
        if (idToken != null) {
            return idToken.getPayload();
        } else {
            throw new IllegalArgumentException("Invalid ID token.");
        }
    }

    public boolean isValidToken(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString);
        if (idToken != null) {
            return true;
        } else {
            throw new GeneralSecurityException("Invalid ID token.");
        }
    }

    public String getProfileDetailsGoogle(String accessToken) {
        try {
            GoogleIdToken.Payload payload = getJwtPayload(accessToken);
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            return "User: " + name + " (Email: " + email + ")";
        } catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return "Token verification failed: " + e.getMessage();
        }
    }

    public UserDetails getUserDetailsFromToken(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdToken.Payload payload = getJwtPayload(idTokenString);
        String email = payload.getEmail();
        String userId = payload.getSubject();

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (adminEmails.contains(email)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return User
                .withUsername(email)
                .password("")
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
