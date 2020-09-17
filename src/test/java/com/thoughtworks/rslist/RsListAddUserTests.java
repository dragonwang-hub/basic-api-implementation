package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.Conversions.string;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsListAddUserTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void should_add_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
        User user = new User("dragon", 24, "male", "ylw@tw.com", "18812345678");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
        ObjectMapper objectMapper = new ObjectMapper();// 通过此类实现json的序列化和反序列化
        String json = objectMapper.writerWithView(RsEvent.Internal.class).writeValueAsString(rsEvent); // 转为json字符串
        mockMvc.perform(post("/rs/event").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(header().string("Location",is("/rs/4")));
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyword", is("经济")));
    }

    @Test
    void should_badrequest_when_user_is_null() throws Exception {
        User user = new User("dragon", 24, "male", "ylw@tw.com", "18812345678");
        RsEvent rsEvent = new RsEvent("", "经济", null);
        ObjectMapper objectMapper = new ObjectMapper();// 通过此类实现json的序列化和反序列化
        String json = objectMapper.writeValueAsString(rsEvent); // 转为json字符串
        mockMvc.perform(post("/rs/event").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_badrequest_when_add_rsevent_eventName_is_empty() throws Exception {
        User user = new User("dragon", 24, "male", "ylw@tw.com", "18812345678");
        RsEvent rsEvent = new RsEvent("", "经济", user);
        ObjectMapper objectMapper = new ObjectMapper();// 通过此类实现json的序列化和反序列化
        String json = objectMapper.writeValueAsString(rsEvent); // 转为json字符串
        mockMvc.perform(post("/rs/event").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_badrequest_when_add_rsevent_keywork_is_empty() throws Exception {
        User user = new User("dragon", 24, "male", "ylw@tw.com", "18812345678");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "", user);
        ObjectMapper objectMapper = new ObjectMapper();// 通过此类实现json的序列化和反序列化
        String json = objectMapper.writeValueAsString(rsEvent); // 转为json字符串
        mockMvc.perform(put("/rs/event").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_and_user_is_existed() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
        User user = new User("hello", 19, "male", "1@2.3", "10123456789");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
        ObjectMapper objectMapper = new ObjectMapper();// 通过此类实现json的序列化和反序列化
        String json = objectMapper.writerWithView(RsEvent.Internal.class).writeValueAsString(rsEvent); // 转为json字符串
        mockMvc.perform(post("/rs/event").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyword", is("经济")))
                .andExpect(jsonPath("$", not(hasKey("user"))));
    }
}
