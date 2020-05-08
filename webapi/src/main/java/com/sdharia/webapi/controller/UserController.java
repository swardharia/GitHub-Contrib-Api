package com.sdharia.webapi.controller;

import com.sdharia.exception.ApiRequestException;
import com.sdharia.model.GitResponse;
import com.sdharia.model.UserList;
import com.sdharia.service.GithubService;
import com.sdharia.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/gitcontrib")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GithubService githubService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public UserList getUsers(@RequestParam String location, @RequestParam(defaultValue = "50") @Min(1) @Max(150) int toplimit){
        Flux<GitResponse> response= githubService.getGitResponse(location, toplimit);
        List<GitResponse> gitResponseList = response.collectList().block();
        return userService.getUsers(gitResponseList);
    }

}