package com.mkyong;

public class Metadata {

    private String user_agent;
    private String platform;
    private String referer;
    private String network_id;
    private String browser;

    public Metadata() {

    }

    public Metadata(String user_agent, String platform, String referer, String network_id, String browser) {
        this.browser = browser;
        this.user_agent = user_agent;
        this.referer = referer;
        this.network_id = network_id;
        this.platform = platform;
    }

    public String getBrowser() {
        return browser;
    }

    public String getNetwork_id() {
        return network_id;
    }

    public String getPlatform() {
        return platform;
    }

    public String getReferer() {
        return referer;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public void setNetwork_id(String network_id) {
        this.network_id = network_id;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }
}