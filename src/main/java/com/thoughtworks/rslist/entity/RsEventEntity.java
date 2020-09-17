package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    //private int userId;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
