package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    public interface Public {}
    public interface Internal extends Public {}

    @NotEmpty //注释掉便于通过新测试-》should_update_rsevent_when_update_eventname
    @JsonView(RsEvent.Public.class)
    private String eventName;

    @NotEmpty
    @JsonView(RsEvent.Public.class)
    private String keyword;

    //@NotNull 注释掉-便于不带此参数传递时可以通过Valid校验，因此判断此为null的Test将失效-》should_badrequest_when_user_is_null
    @Valid
    @JsonView(RsEvent.Internal.class)
    private User user;

    @NotNull
    @JsonView(RsEvent.Public.class)
    private int userId;

    @JsonView(RsEvent.Public.class)
    private int rsEventId;

    @JsonView(RsEvent.Public.class)
    private int rsVotes;

    @JsonIgnore
    public int getUserId() {
        return userId;
    }

    @JsonProperty
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public RsEvent(@NotEmpty String eventName, @NotEmpty String keyword, @NotNull int userId) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.userId = userId;
    }

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }

    public RsEvent(@NotEmpty String eventName, @NotEmpty String keyword, @NotNull @Valid User user) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.user = user;
    }
}
