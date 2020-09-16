package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private List<User> userList = initRsList();

    private List<User> initRsList() {
        List<User> tempUserList = new ArrayList<>();
        tempUserList.add(new User("hello", 10, "male", "1@2.3", "10123456789"));
        tempUserList.add(new User("kityy", 10, "female", "1@2.3", "10123456789"));
        return tempUserList;
    }

    @GetMapping("/rs/users")
    public List<User> getAllUser() {
        return userList;
    }

    @PostMapping("/rs/register")
    public void registerUser(@RequestBody String userInfo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        User registerUser = objectMapper.readValue(userInfo, User.class);
        userList.add(registerUser);
    }
}

