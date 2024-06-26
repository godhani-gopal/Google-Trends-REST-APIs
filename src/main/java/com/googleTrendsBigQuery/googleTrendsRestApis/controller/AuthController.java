package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.service.AuthService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication API")
public class AuthController {

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "Handle Google OAuth2 callback and set JWT cookie")
    @ApiResponses(value = {
            @ApiResponse(code = 302, message = "Redirect to Swagger UI"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping(value = {"/oauth2/callback"})
    public ResponseEntity<String> handleAuthentication(
            @ApiParam(value = "Authorization code from Google", required = true)
            @RequestParam("code") String code, HttpServletResponse response) {

        String accessToken = authService.getJwtToken(code);

        ResponseCookie cookies = ResponseCookie.from("jwt", accessToken)
                .httpOnly(true)
                .path("/")
                .maxAge(12 * 60 * 60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookies.toString());
        
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, "/swagger-ui/index.html#/")
                .body("Google Login Successful! Redirecting to Swagger UI...");
    }
}