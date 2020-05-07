package com.sdharia.serviceimpl;

import com.sdharia.config.AppProperties;
import com.sdharia.model.GitResponse;
import com.sdharia.service.GithubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

@Service("GitHubServiceImpl")
public class GitHubServiceImpl implements GithubService {

    private static final String GITHUB_V3_MIME_TYPE = "application/vnd.github.v3+json";
    private static final String GITHUB_API_BASE_URL = "https://api.github.com/graphql";
    private static final String USER_AGENT = "Spring 5 WebClient";
    private static final Logger logger = LoggerFactory.getLogger(GitHubServiceImpl.class);

    private final WebClient webClient;

    @Autowired
    public GitHubServiceImpl(AppProperties appProperties) {
        this.webClient = WebClient.builder()
                .baseUrl(GITHUB_API_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, GITHUB_V3_MIME_TYPE)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .filter(ExchangeFilterFunctions
                        .basicAuthentication(appProperties.getGithub().getUsername(),
                                appProperties.getGithub().getToken()))
                .filter(logRequest())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
            return next.exchange(clientRequest);
        };
    }

    @Override
    public GitResponse getGitResponse(String location){
        GitResponse response = webClient.post()
                .uri(GITHUB_API_BASE_URL)
                .body(BodyInserters.fromObject(queryJsonBuilder(location, "")))
                .retrieve()
                .bodyToMono(GitResponse.class)
                .block();

        return response;
    }

    @Override
    public String queryJsonBuilder(String location, String start){

        String after = start == "" ? "" : "after:\\\""+start+"\\\",";
        String query_json = "{\"query\":\"query {\\n            search(query: \\\"location:"+ location +"\\\","+after+"type: USER, " +
                "first: 100) {\\npageInfo {\\n      startCursor\\n      hasNextPage\\n      endCursor\\n    }" +
                "\\n              userCount\\n              edges {\\n      node {\\n        ... on User " +
                "{\\n          login\\n          name\\n          location\\nrepositories {totalCount}\\n        }" +
                "\\n      }\\n    }\\n            }\\n          }\"}";

        return query_json;
    }
}
