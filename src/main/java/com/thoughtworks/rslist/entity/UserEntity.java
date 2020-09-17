package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.dto.RsEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "user")
    private String userName;

    private int age;

    private String gender;

    private String email;

    private String phone;

    private int voteNumb;

    @OneToMany(mappedBy = "user", cascade=CascadeType.REMOVE)// 此处mappedby属性等于RsEntity表的user属性
    private List<RsEventEntity> rsEventEntities;
}
