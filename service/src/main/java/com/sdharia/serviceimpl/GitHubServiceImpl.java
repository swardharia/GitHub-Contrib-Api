package com.sdharia.serviceimpl;

import com.sdharia.config.AppProperties;
import com.sdharia.model.GitResponse;
import com.sdharia.service.GithubService;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("GitHubServiceImpl")
public class GitHubServiceImpl implements GithubService {

    private static final String GITHUB_V3_MIME_TYPE = "application/vnd.github.v3+json";
    private static final String GITHUB_API_BASE_URL = "https://api.github.com/graphql";
    private static final String USER_AGENT = "Spring 5 WebClient";
    private static final Logger logger = LoggerFactory.getLogger(GitHubServiceImpl.class);

    private final WebClient webClient;
    private static String start = "";

    @Autowired
    public GitHubServiceImpl(AppProperties appProperties) {
        this.webClient = WebClient.builder()
                .baseUrl(GITHUB_API_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, GITHUB_V3_MIME_TYPE)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .filter(ExchangeFilterFunctions
                        .basicAuthentication(appProperties.getGithub().getUsername(),
                                appProperties.getGithub().getToken()))
                .build();
    }

    @Override
    public Flux<GitResponse> getGitResponse(String location, int toplimit){
        start = "";
        int remainder = toplimit > 100 ? toplimit%100 : 0;
        toplimit = toplimit - remainder;

        if(remainder != 0)
            start = getNextPage(location);

        List<Pair<Integer,String>> limits = new ArrayList<>(Arrays.asList(
                new Pair<>(toplimit, ""), new Pair<>(remainder, start)));

        return Flux.fromIterable(limits)
                .parallel()
                .runOn(Schedulers.elastic())
                .flatMap(limit-> limit.getKey()==0 ? Mono.empty() : this.getResponse(location, limit.getKey(), limit.getValue()))
                .ordered((u1, u2) -> u2.getData().getSearch().getEdges().get(0).getNode().getRepositories().getTotalCount() -
                        u1.getData().getSearch().getEdges().get(0).getNode().getRepositories().getTotalCount());
    }

    public Mono<GitResponse> getResponse(String location, int toplimit, String start) {

        return webClient.post()
                .uri(GITHUB_API_BASE_URL)
                .body(BodyInserters.fromObject(queryJsonBuilder(location, toplimit, start)))
                .retrieve()
                .bodyToMono(GitResponse.class);
    }

    @Override
    public String queryJsonBuilder(String location, int toplimit, String start){

        String after = start == "" ? "" : "after:\\\""+start+"\\\",";
        String query_json = "{\"query\":\"query {\\n            search(query: \\\"location:"+ location +" sort:repositories-desc\\\","+after+"type: USER, " +
                "first: "+toplimit+") {\\npageInfo {\\n      startCursor\\n      hasNextPage\\n      endCursor\\n    }" +
                "\\n              userCount\\n              edges {\\n      node {\\n        ... on User " +
                "{\\n          login\\n          name\\n          location\\nrepositories {totalCount}\\n        }" +
                "\\n      }\\n    }\\n            }\\n          }\"}";

        return query_json;
    }

    public String getNextPage(String location) {
        String query_json = "{\"query\":\"query {\\n            search(query: \\\"location:"+ location +"\\\", " +
                "first: 100 type: USER) {\\n    pageInfo {\\n      startCursor\\n      hasNextPage\\n      " +
                "endCursor\\n    }\\n  }\\n\\n\\n          }\"}";
        GitResponse pageResponse = webClient.post()
                .uri(GITHUB_API_BASE_URL)
                .body(BodyInserters.fromObject(query_json))
                .retrieve()
                .bodyToMono(GitResponse.class)
                .block();
        return pageResponse.getData().getSearch().getPageInfo().getEndCursor();
    }
}
