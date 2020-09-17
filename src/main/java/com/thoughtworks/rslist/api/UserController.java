package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.userrepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

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
        UserEntity userEntity = UserEntity.builder()
                .userName(newUser.getUserName())
                .age(newUser.getAge())
                .email(newUser.getEmail())
                .gender(newUser.getGender())
                .phone(newUser.getPhone())
                .build();
        userRepository.save(userEntity);
        userList.add(newUser);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/rs/users/{id}")
    public ResponseEntity<Optional<UserEntity>> getUserById(@PathVariable int id) {
        Optional<UserEntity> userById = userRepository.findById(id);
        return ResponseEntity.ok(userById);
    }

    @DeleteMapping("/rs/users/{id}")
    public ResponseEntity deleteRsEvent(@PathVariable int id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity handleIndexOutOfBoundsException(Exception ex) throws JsonProcessingException {
        CommentError commentError = new CommentError();
        commentError.setError("invalid user");
        return ResponseEntity.badRequest().body(commentError);
    }
}

