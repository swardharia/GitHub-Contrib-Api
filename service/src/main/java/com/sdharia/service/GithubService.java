package com.sdharia.service;

import com.sdharia.model.GitResponse;
import org.springframework.stereotype.Service;

@Service("githubService")
public interface GithubService {
    public GitResponse getGitResponse(String location);
    public String queryJsonBuilder(String location, String start);
}
