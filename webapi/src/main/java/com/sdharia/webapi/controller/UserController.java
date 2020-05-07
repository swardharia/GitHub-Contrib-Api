package com.sdharia.controller;

import com.sdharia.model.GitResponse;
import com.sdharia.model.User;
import com.sdharia.service.GithubService;
import com.sdharia.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/gitcontrib")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GithubService githubService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public List<User> getUsers(@RequestParam String location, @RequestParam(defaultValue = "100") String toplimit){
        GitResponse response= githubService.getGitResponse(location);
        logger.info("Total count = "+response.getData().getSearch().getUserCount());
        //return userService.getUsers();
        return Collections.emptyList();
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> userControllerException(WebClientResponseException ex) {
        logger.error("Error from UserController - Status {}, Body {}", ex.getRawStatusCode(),
                ex.getResponseBodyAsString(), ex);
        return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
    }
}
