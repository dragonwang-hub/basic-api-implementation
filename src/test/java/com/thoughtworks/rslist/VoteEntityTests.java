package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.userrepository.RsEventRepository;
import com.thoughtworks.rslist.userrepository.UserRepository;
import com.thoughtworks.rslist.userrepository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VoteEntityTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    LocalDateTime localDateTime;

    @BeforeEach
    public void setUp() {
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        localDateTime = LocalDateTime.now();
        setData();
    }

    UserEntity userEntity_1;
    RsEventEntity rsEventEntity_1;

    private void setData() {
        userEntity_1 = UserEntity.builder()
                .userName("hello")
                .age(19)
                .gender("male")
                .email("1@2.3")
                .phone("10123456789")
                .build();
        UserEntity userEntity_2 = UserEntity.builder()
                .userName("kitty")
                .age(19)
                .gender("female")
                .email("1@2.3")
                .phone("10123456789")
                .build();
        UserEntity userEntity_3 = UserEntity.builder()
                .userName("dragon")
                .age(24)
                .gender("male")
                .email("1@2.3")
                .phone("10123456789")
                .build();
        List<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userEntity_1);
        userEntityList.add(userEntity_2);
        userEntityList.add(userEntity_3);
        userRepository.saveAll(userEntityList);

        rsEventEntity_1 = RsEventEntity.builder()
                .eventName("猪肉又涨价了啊！")
                .keyword("经济")
                .user(userEntity_1)
                .build();
        RsEventEntity rsEventEntity_2 = RsEventEntity.builder()
                .eventName("猪肉什么时候降价")
                .keyword("经济")
                .user(userEntity_1)
                .build();
        List<RsEventEntity> rsEventEntityList = new ArrayList<>();
        rsEventEntityList.add(rsEventEntity_1);
        rsEventEntityList.add(rsEventEntity_2);
        rsEventRepository.saveAll(rsEventEntityList);

        VoteEntity vote_1 = VoteEntity.builder()
                .rsEvent(rsEventEntity_1)
                .user(userEntity_1)
                .voteNumb(2)
                .curTime(localDateTime)
                .build();
        VoteEntity vote_2 = VoteEntity.builder()
                .rsEvent(rsEventEntity_1)
                .user(userEntity_2)
                .voteNumb(2)
                .curTime(localDateTime)
                .build();
        VoteEntity vote_3 = VoteEntity.builder()
                .rsEvent(rsEventEntity_1)
                .user(userEntity_3)
                .voteNumb(2)
                .curTime(localDateTime)
                .build();
        VoteEntity vote_4 = VoteEntity.builder()
                .rsEvent(rsEventEntity_2)
                .user(userEntity_1)
                .voteNumb(2)
                .curTime(localDateTime)
                .build();
        VoteEntity vote_5 = VoteEntity.builder()
                .rsEvent(rsEventEntity_2)
                .user(userEntity_2)
                .voteNumb(2)
                .curTime(localDateTime)
                .build();
        VoteEntity vote_6 = VoteEntity.builder()
                .rsEvent(rsEventEntity_2)
                .user(userEntity_3)
                .voteNumb(2)
                .curTime(localDateTime)
                .build();
        List<VoteEntity> voteEntityList = new ArrayList<>();
        voteEntityList.add(vote_1);
        voteEntityList.add(vote_2);
        voteEntityList.add(vote_3);
        voteEntityList.add(vote_4);
        voteEntityList.add(vote_5);
        voteEntityList.add(vote_6);
        voteRepository.saveAll(voteEntityList);
    }

    @Test
    void should_vote_success_when_user_have_enough_votes() throws Exception {
        Vote vote = new Vote(5, 1, localDateTime);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonVote = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/1").content(jsonVote).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void should_vote_failed_when_user_have_noenough_votes() throws Exception {
        UserEntity user = UserEntity.builder()
                .userName("dragon")
                .gender("male")
                .age(24)
                .phone("18812345678")
                .email("ylw@tw.com")
                .voteNumb(10)
                .build();
        userRepository.save(user);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("猪肉又涨价了啊！")
                .keyword("经济")
                .user(user)
                .build();
        rsEventRepository.save(rsEventEntity);
        Vote firstVote = new Vote(5, 1, localDateTime);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFirstVote = objectMapper.writeValueAsString(firstVote);
        mockMvc.perform(post("/rs/vote/1").content(jsonFirstVote).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Vote secondVote = new Vote(6, 1, localDateTime);
        String jsonSecondVote = objectMapper.writeValueAsString(secondVote);
        mockMvc.perform(post("/rs/vote/1").content(jsonSecondVote).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_get_votes_by_userId_and_rsEventId() throws Exception {
        mockMvc.perform(get("/rs/vote")
                .param("userId", String.valueOf(userEntity_1.getId()))
                .param("rsEventId", String.valueOf(rsEventEntity_1.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(userEntity_1.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventEntity_1.getId())))
                .andExpect(jsonPath("$[0].voteNumb", is(2)));
    }

    @Test
    void should_get_votes_of_specify_page_by_pageable_when_setup_pageable_for_findall() throws Exception {
        mockMvc.perform(get("/rs/votes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userEntity_1.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventEntity_1.getId())))
                .andExpect(jsonPath("$[0].voteNumb", is(2)));
        mockMvc.perform(get("/rs/votes/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

}
