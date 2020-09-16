package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    public interface Public {}
    public interface Internal extends Public {}

    @NotEmpty
    @JsonView(RsEvent.Public.class)
    private String eventName;
    @NotEmpty
    @JsonView(RsEvent.Public.class)
    private String keyword;
    @NotNull
    @Valid
    @JsonView(RsEvent.Internal.class)
    private User user;

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }
}
