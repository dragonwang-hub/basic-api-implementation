package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    public static List<User> userList = initUserList();

    private static List<User> initUserList() {
        List<User> tempList = new ArrayList<>();
        tempList.add(new User("hello", 19, "male", "1@2.3", "10123456789"));
        tempList.add(new User("kityy", 19, "female", "1@2.3", "10123456789"));
        return tempList;
    }

    @GetMapping("/rs/users")
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(userList);
    }

    @PostMapping("/rs/register")
    public ResponseEntity registerUser(@Valid @RequestBody User newUser) throws JsonProcessingException {
        userList.add(newUser);
        return ResponseEntity.created(null).build();
    }
}

