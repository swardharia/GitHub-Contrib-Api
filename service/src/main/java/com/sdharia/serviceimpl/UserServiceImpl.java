package com.sdharia.serviceimpl;

import com.sdharia.model.GitResponse;
import com.sdharia.model.User;
import com.sdharia.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Collections;
import java.util.List;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {

    @Override
    public List<User> getUsers(Mono<GitResponse> response){
        return Collections.emptyList();
    }
}
