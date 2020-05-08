package com.sdharia.model;

public class User {

    private String login;
    private String name;
    private String location;
    private int contributions;

    public User(String login, String name, String location, int contributions) {
        this.login = login;
        this.name = name;
        this.location = location;
        this.contributions = contributions;
    }

    public User() {

    }

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

    public int getContributions() {
        return contributions;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }
}
