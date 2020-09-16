package com.thoughtworks.rslist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserValidateTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void should_return_register_success_when_register_user_info_is_valid() throws Exception {
        User user = new User("dragon", 24, "male", "ylw@tw.com", "18812345678");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(get("/rs/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        mockMvc.perform(post("/rs/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void should_return_register_failed_when_userName_is_empty() throws Exception {
        User user = new User("", 19, "male", "ylw@tw.com", "18812345678");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_register_failed_when_userName_size_more_than_8() throws Exception {
        User user = new User("dragon_wang", 19, "male", "ylw@tw.com", "18812345678");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_register_failed_when_age_less_than_18() throws Exception {
        User user = new User("dragon", 17, "male", "ylw@tw.com", "18812345678");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_register_failed_when_age_more_than_100() throws Exception {
        User user = new User("dragon", 101, "male", "ylw@tw.com", "18812345678");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_register_failed_when_male_is_emprty() throws Exception {
        User user = new User("dragon", 20, "", "ylw@tw.com", "18812345678");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_register_failed_when_email_is_invaild() throws Exception {
        User user = new User("dragon", 20, "male", "@tw.com", "18812345678");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_register_failed_when_phone_is_invaild() throws Exception {
        User user = new User("dragon", 20, "male", "ylw@tw.com", "2881234567");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
