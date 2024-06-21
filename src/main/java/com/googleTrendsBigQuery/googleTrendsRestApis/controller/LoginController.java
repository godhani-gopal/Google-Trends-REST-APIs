package com.googleTrendsBigQuery.googleTrendsRestApis.controller;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.LogInService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {


   LogInService logInService;

    public LoginController(LogInService logInService) {
        this.logInService = logInService;
    }

    @GetMapping("/grantcode")
    public String grantCode(@RequestParam("code") String code) {
        String accessToken = logInService.getOauthAccessTokenGoogle(code);
        String profileDetails = logInService.getProfileDetailsGoogle(accessToken);
        System.out.println("Access Token in grantCode method: " + accessToken); // Print token to console
        return profileDetails;
    }

}