package com.sdharia.service;

import com.sdharia.model.GitResponse;
import com.sdharia.model.UserList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public interface UserService {
    public UserList getUsers(List<GitResponse> gitResponseList);
}
