package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteEntity {
    @Id
    @GeneratedValue
    private Integer id;

    private int voteNumb;

    private LocalDateTime voteTime;

    @ManyToOne()
    @JoinColumn(name = "user_Id")
    private UserEntity user;

    @ManyToOne()
    @JoinColumn(name = "rsEvent_Id")
    private RsEventEntity rsEvent;
}
