package com.thoughtworks.rslist;

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
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserEntityTests {
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
    void should_add_user_to_mysql_when_register_user_info_is_valid() throws Exception {
        User user = new User("dragon", 24, "male", "ylw@tw.com", "18812345678");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        // 用于测试是否成功在数据库内add数据
        List<UserEntity> allUser = userRepository.findAll();
        assertEquals(1, allUser.size());
        assertEquals("dragon",allUser.get(0).getUserName());
    }

    @Test
    void should_get_user_from_mysql_when_want_user_info_by_userid() throws Exception {
        User user = new User("dragon", 24, "male", "ylw@tw.com", "18812345678");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName",is("dragon")));
    }

    @Test
    void should_delete_user_from_mysql_when_delete_user_by_userid() throws Exception {
        UserEntity user = UserEntity.builder()
                .userName("dragon")
                .gender("male")
                .age(24)
                .phone("18812345678")
                .email("ylw@tw.com")
                .voteNumb(10)
                .build();
        userRepository.save(user);
        mockMvc.perform(delete("/rs/users/1"))
                .andExpect(status().isNoContent());
        List<UserEntity> allUser = userRepository.findAll();
        assertEquals(0, allUser.size());
    }

    @Test
    void should_delete_rsevent_of_user_when_delete_user() throws Exception {
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
                .eventName("猪肉什么时候能降价？")
                .keyword("民生")
                .user(user)
                .build();
        rsEventRepository.save(rsEventEntity);
        List<RsEventEntity> allRsEvent = rsEventRepository.findAll();
        assertEquals(1, allRsEvent.size());
        mockMvc.perform(delete("/rs/users/1"))
                .andExpect(status().isNoContent());
        List<UserEntity> allUser = userRepository.findAll();
        assertEquals(0, allUser.size());
        List<RsEventEntity> allRsEventlater = rsEventRepository.findAll();
        assertEquals(0, allRsEventlater.size());
    }
}
