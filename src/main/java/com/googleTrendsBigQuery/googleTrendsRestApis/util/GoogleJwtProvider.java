package com.googleTrendsBigQuery.googleTrendsRestApis.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
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
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GoogleJwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(GoogleJwtProvider.class);
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.redirect-uri}")
    private String redirectUri;

    @Value("${google.oauth2.token-url}")
    private String tokenUrl;

    @Value("${oauth.google.scopes}")
    private String oauthScopes;

    @Value("${jwt.admin.emails}")
    private String adminEmails;

    public GoogleJwtProvider(GoogleIdTokenVerifier googleIdTokenVerifier) {
        this.googleIdTokenVerifier = googleIdTokenVerifier;
    }

    private Set<String> getAdminEmails() {
        return Stream.of(adminEmails.split(",")).map(String::trim).collect(Collectors.toSet());
    }

    public String getOauthIdTokenGoogle(String code) {
        try {
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
        } catch (Exception e) {
            logger.error("Error retrieving token from Google callback request body.", e);
            throw new AuthenticationException("Cannot retrieve token: ", e) {};
        }
    }

    public GoogleIdToken.Payload getJwtPayload(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString);
        if (idToken != null) {
            return idToken.getPayload();
        } else {
            logger.error("Invalid ID token received.");
            throw new GeneralSecurityException("Invalid ID token.");
        }
    }

    public boolean isValidToken(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString);
        if (idToken != null) {
            return true;
        } else {
            logger.error("Invalid ID token received during validation.");
            throw new GeneralSecurityException("Invalid ID token.");
        }
    }

    public String getGoogleProfileDetails(String accessToken) {
        try {
            GoogleIdToken.Payload payload = getJwtPayload(accessToken);
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            return "User: " + name + " (Email: " + email + ")";
        } catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
            logger.error("Error while retrieving Google profile details.", e);
            throw new RuntimeException("Token verification failed: " + e.getMessage());
        }
    }

    public UserDetails getUserDetailsFromToken(String idTokenString) throws GeneralSecurityException {
        try {
            GoogleIdToken.Payload payload = getJwtPayload(idTokenString);
            String email = payload.getEmail();

            List<GrantedAuthority> authorities = new ArrayList<>();

            if (getAdminEmails().contains(email)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            logger.info("User with email {} and role {} logging in.", email, authorities);
            return User
                    .withUsername(email)
                    .password("")
                    .authorities(authorities)
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
        } catch (Exception e) {
            logger.error("Error creating or validating UserDetails.", e);
            throw new RuntimeException("Error creating or validating UserDetails.", e);
        }
    }
}
