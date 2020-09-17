package com.thoughtworks.rslist.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "rs_event")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventEntity {

    @Id
    @GeneratedValue
    private int id;

    private String eventName;

    private String keyword;

    private int userId;

}
