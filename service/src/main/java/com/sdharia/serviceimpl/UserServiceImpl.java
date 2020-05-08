package com.sdharia.serviceimpl;

import com.sdharia.model.GitResponse;
import com.sdharia.model.User;
import com.sdharia.model.UserList;
import com.sdharia.service.UserService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sdharia.model.GitResponse.Data.Search.Edges;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {

    @Override
    public UserList getUsers(List<GitResponse> gitResponseList){

        List<User> userList = new ArrayList();
        String defaultValue = "field is private";
        for(GitResponse gresponse : gitResponseList){
            for(Edges edge : gresponse.getData().getSearch().getEdges()){
                userList.add(new User(Optional.ofNullable(edge.getNode().getName()).orElse(defaultValue),
                        Optional.ofNullable(edge.getNode().getLogin()).orElse(defaultValue),
                        Optional.ofNullable(edge.getNode().getLocation()).orElse(defaultValue),
                        edge.getNode().getRepositories() == null ? 0 : edge.getNode().getRepositories().getTotalCount()));
            }
        }
        return new UserList(userList);
    }
}
