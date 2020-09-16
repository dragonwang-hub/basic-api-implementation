package com.thoughtworks.rslist.dto;

public class RsEvent {
    private String eventName;
    private String keyword;
    private User user;

    public RsEvent() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
