package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    @NotEmpty
    private String eventName;
    @NotEmpty
    private String keyword;
    @NotNull
    @Valid
    private User user;

    public static class Public {}
    public static class Internal extends Public {}

    @JsonView(RsEvent.Public.class)
    public String getEventName() {
        return eventName;
    }
    @JsonView(RsEvent.Public.class)
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @JsonView(RsEvent.Public.class)
    public String getKeyword() {
        return keyword;
    }
    @JsonView(RsEvent.Public.class)
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @JsonView(RsEvent.Internal.class)
    public User getUser() {
        return user;
    }

    @JsonView(RsEvent.Internal.class)
    public void setUser(User user) {
        this.user = user;
    }

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }
}
