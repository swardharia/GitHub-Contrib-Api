package com.sdharia.service;

import com.sdharia.model.GitResponse;
import com.sdharia.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service("userService")
public interface UserService {
    public List<User> getUsers(Mono<GitResponse> response);
}
