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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
    public void setUp(){
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void should_vote_success_when_user_have_enough_votes() throws Exception {
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
        Vote vote = new Vote(5,1, "currenttime");
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
        Vote firstVote = new Vote(5,1, "currenttime");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFirstVote = objectMapper.writeValueAsString(firstVote);
        mockMvc.perform(post("/rs/vote/1").content(jsonFirstVote).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Vote secondVote = new Vote(6,1, "currenttime");
        String jsonSecondVote = objectMapper.writeValueAsString(secondVote);
        mockMvc.perform(post("/rs/vote/1").content(jsonSecondVote).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
