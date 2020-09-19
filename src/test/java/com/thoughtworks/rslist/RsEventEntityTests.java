package com.thoughtworks.rslist;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.userrepository.RsEventRepository;
import com.thoughtworks.rslist.userrepository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RsEventEntityTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    public void setUp(){
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void should_add_rsevent_when_user_exists() throws Exception {
        UserEntity user = UserEntity.builder()
                .userName("dragon")
                .gender("male")
                .age(24)
                .phone("18812345678")
                .email("ylw@tw.com")
                .voteNumb(10)
                .build();
        userRepository.save(user);

        RsEvent rsEvent = new RsEvent("猪肉什么时候能降价？", "民生", user.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithView(RsEvent.Public.class).writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventEntity> allRsEvent = rsEventRepository.findAll();
        assertEquals(1, allRsEvent.size());
        assertEquals("猪肉什么时候能降价？", allRsEvent.get(0).getEventName());
        assertEquals(user.getId(), allRsEvent.get(0).getUser().getId());
    }

    @Test
    void should_bad_request_when_user_not_exists() throws Exception {
        UserEntity user = UserEntity.builder()
                .userName("dragon")
                .gender("male")
                .age(24)
                .phone("18812345678")
                .email("ylw@tw.com")
                .voteNumb(10)
                .build();
        userRepository.save(user);

        RsEvent rsEvent = new RsEvent("猪肉什么时候能降价？", "民生", 2);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithView(RsEvent.Public.class).writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        List<RsEventEntity> allRsEvent = rsEventRepository.findAll();
        assertEquals(0, allRsEvent.size());
    }

    @Test
    void should_update_rsevent_when_put_new_rsevent() throws Exception {
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
        RsEvent rsEvent = new RsEvent("猪肉什么时候能降价？", "民生", user.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithView(RsEvent.Public.class).writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/events/1").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<RsEventEntity> allRsEvent = rsEventRepository.findAll();
        assertEquals(1, allRsEvent.size());
        assertEquals("猪肉什么时候能降价？", allRsEvent.get(0).getEventName());
        assertEquals("民生", allRsEvent.get(0).getKeyword());
        assertEquals(user.getUserName(), allRsEvent.get(0).getUser().getUserName());
    }

    @Test
    void should_update_rsevent_when_user_is_not_same() throws Exception {
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
        UserEntity newuser = UserEntity.builder()
                .userName("qing")
                .gender("female")
                .age(24)
                .phone("18812345678")
                .email("q@tw.com")
                .voteNumb(10)
                .build();
        userRepository.save(newuser);
        RsEvent rsEvent = new RsEvent("猪肉什么时候能降价？", "民生", newuser.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithView(RsEvent.Public.class).writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/events/1").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        List<RsEventEntity> allRsEvent = rsEventRepository.findAll();
        assertEquals(1, allRsEvent.size());
        assertEquals("猪肉又涨价了啊！", allRsEvent.get(0).getEventName());
        assertEquals("经济", allRsEvent.get(0).getKeyword());
        assertEquals(user.getUserName(), allRsEvent.get(0).getUser().getUserName());
    }

    @Test
    void should_update_rsevent_when_update_eventname() throws Exception {
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
        RsEvent rsEvent = new RsEvent("猪肉什么时候能降价？", "", user.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithView(RsEvent.Public.class).writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/events/1").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<RsEventEntity> allRsEvent = rsEventRepository.findAll();
        assertEquals(1, allRsEvent.size());
        assertEquals("猪肉什么时候能降价？", allRsEvent.get(0).getEventName());
        assertEquals("经济", allRsEvent.get(0).getKeyword());
        assertEquals(user.getUserName(), allRsEvent.get(0).getUser().getUserName());
    }

    @Test
    void should_update_rsevent_when_update_key() throws Exception {
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
        RsEvent rsEvent = new RsEvent("", "民生", user.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithView(RsEvent.Public.class).writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/events/1").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<RsEventEntity> allRsEvent = rsEventRepository.findAll();
        assertEquals(1, allRsEvent.size());
        assertEquals("猪肉又涨价了啊！", allRsEvent.get(0).getEventName());
        assertEquals("民生", allRsEvent.get(0).getKeyword());
        assertEquals(user.getUserName(), allRsEvent.get(0).getUser().getUserName());
    }
}
