package com.sdharia.service;

import com.sdharia.model.GitResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service("githubService")
public interface GithubService {
    public Flux<GitResponse> getGitResponse(String location, int toplimit);
    public String queryJsonBuilder(String location, int toplimit, String start);
    public String getNextPage(String location);
}
