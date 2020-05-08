package com.sdharia.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        private Search search;

        public Search getSearch() {
            return search;
        }

        public void setSearch(Search search) {
            this.search = search;
        }

        public static class Search{
            private PageInfo pageInfo;
            private int userCount;
            private List<Edges> edges;

            public PageInfo getPageInfo() {
                return pageInfo;
            }

            public void setPageInfo(PageInfo pageInfo) {
                this.pageInfo = pageInfo;
            }

            public int getUserCount() {
                return userCount;
            }

            public void setUserCount(int userCount) {
                this.userCount = userCount;
            }

            public List<Edges> getEdges() {
                return edges;
            }

            public void setEdges(List<Edges> edges) {
                this.edges = edges;
            }

            public static class PageInfo{
                private String startCursor;
                private boolean hasNextPage;
                private String endCursor;

                public String getStartCursor() {
                    return startCursor;
                }

                public void setStartCursor(String startCursor) {
                    this.startCursor = startCursor;
                }

                public boolean isHasNextPage() {
                    return hasNextPage;
                }

                public void setHasNextPage(boolean hasNextPage) {
                    this.hasNextPage = hasNextPage;
                }

                public String getEndCursor() {
                    return endCursor;
                }

                public void setEndCursor(String endCursor) {
                    this.endCursor = endCursor;
                }
            }

            public static class Edges{
                private Nodes node;

                public Nodes getNode() {
                    return node;
                }

                public void setNode(Nodes node) {
                    this.node = node;
                }

                public static class Nodes{
                    private String login;
                    private String name;
                    private String location;
                    private Repositories repositories;

                    public String getLogin() {
                        return login;
                    }

                    public void setLogin(String login) {
                        this.login = login;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getLocation() {
                        return location;
                    }

                    public void setLocation(String location) {
                        this.location = location;
                    }

                    public Repositories getRepositories() {
                        return repositories;
                    }

                    public void setRepositories(Repositories repositories) {
                        this.repositories = repositories;
                    }

                    public static class Repositories{
                        private int totalCount;

                        public int getTotalCount() {
                            return totalCount;
                        }

                        public void setTotalCount(int totalCount) {
                            this.totalCount = totalCount;
                        }
                    }
                }
            }
        }
    }
}











