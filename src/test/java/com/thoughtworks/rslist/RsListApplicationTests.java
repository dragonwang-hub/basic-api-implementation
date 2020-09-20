package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.userrepository.RsEventRepository;
import com.thoughtworks.rslist.userrepository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void should_get_all_rs_event() throws Exception {
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
                .eventName("第一条事件")
                .keyword("无分类")
                .user(user)
                .rsVotes(10)
                .build();
        rsEventRepository.save(rsEventEntity);
        RsEventEntity rsEventEntity_2 = RsEventEntity.builder()
                .eventName("第二条事件")
                .keyword("分类")
                .user(user)
                .rsVotes(10)
                .build();
        rsEventRepository.save(rsEventEntity_2);
        mockMvc.perform(get("/rs/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[0].rsEventId",is(1)))
                .andExpect(jsonPath("$[0].rsVotes", is(10)))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类")))
                .andExpect(jsonPath("$[1].rsEventId",is(2)))
                .andExpect(jsonPath("$[1].rsVotes", is(10)));
    }

    @Test
    void should_get_one_rs_event() throws Exception {
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
                .eventName("第一条事件")
                .keyword("无分类")
                .user(user)
                .rsVotes(10)
                .build();
        rsEventRepository.save(rsEventEntity);
        mockMvc.perform(get("/rs/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyword", is("无分类")))
                .andExpect(jsonPath("$.rsEventId",is(1)))
                .andExpect(jsonPath("$.rsVotes", is(10)));
    }


    @Test
    void should_get_rs_event_by_range() throws Exception {
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
                .eventName("第一条事件")
                .keyword("无分类")
                .user(user)
                .rsVotes(10)
                .build();
        rsEventRepository.save(rsEventEntity);
        RsEventEntity rsEventEntity_2 = RsEventEntity.builder()
                .eventName("第二条事件")
                .keyword("分类")
                .user(user)
                .rsVotes(10)
                .build();
        rsEventRepository.save(rsEventEntity_2);
        mockMvc.perform(get("/rs/events?start=1&end=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[0].rsEventId",is(1)))
                .andExpect(jsonPath("$[0].rsVotes", is(10)))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("分类")))
                .andExpect(jsonPath("$[1].rsEventId",is(2)))
                .andExpect(jsonPath("$[1].rsVotes", is(10)));
    }


    @Test
    void should_delete_one_rs_event() throws Exception {
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
                .eventName("第一条事件")
                .keyword("无分类")
                .user(user)
                .rsVotes(10)
                .build();
        rsEventRepository.save(rsEventEntity);
        RsEventEntity rsEventEntity_2 = RsEventEntity.builder()
                .eventName("第二条事件")
                .keyword("分类")
                .user(user)
                .rsVotes(10)
                .build();
        rsEventRepository.save(rsEventEntity_2);
        mockMvc.perform(get("/rs/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        mockMvc.perform(delete("/rs/events/1"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void should_get_all_rs_event_when_ignore_user() throws Exception {
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
                .eventName("第一条事件")
                .keyword("无分类")
                .user(user)
                .rsVotes(10)
                .build();
        rsEventRepository.save(rsEventEntity);
        mockMvc.perform(get("/rs/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[0].rsEventId",is(1)))
                .andExpect(jsonPath("$[0].rsVotes", is(10)))
                .andExpect(jsonPath("$", not(hasKey("user"))));
    }


    @Test
    void should_return_invalid_request_param_when_start_and_end_not_in_range() throws Exception {
        mockMvc.perform(get("/rs/events?start=1&end=30"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_return_invalid_index_when_index_not_in_range() throws Exception {
        mockMvc.perform(get("/rs/events/30"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }


    @Test
    void should_no_add_a_rs_event_when_name_empty() throws Exception {
        RsEvent rsEvent = new RsEvent(null, "exception!");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_no_add_user_when_user_invalid() throws Exception {
        User user = new User("dragon", 101, "male", "ylw@tw.com", "18812345678");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/users/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }
}
