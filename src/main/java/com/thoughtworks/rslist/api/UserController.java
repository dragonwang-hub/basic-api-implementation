package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.exception.InvalidUserException;
import com.thoughtworks.rslist.userrepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/rs/users")
    public ResponseEntity<List<User>> getAllUser() {
        List<UserEntity> userEntityList = userRepository.findAll();
        List<User> userList = userEntityList.stream().map(userEntity -> User.builder()
                .userName(userEntity.getUserName())
                .age(userEntity.getAge())
                .gender(userEntity.getGender())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .voteNumb(userEntity.getVoteNumb())
                .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(userList);
    }

    @PostMapping("/rs/users/register")
    public ResponseEntity registerUser(@Valid @RequestBody User newUser, BindingResult re) throws InvalidUserException {
        if (re.getAllErrors().size()!= 0) {
            throw new InvalidUserException("invalid user");
        }
        UserEntity userEntity = UserEntity.builder()
                .userName(newUser.getUserName())
                .age(newUser.getAge())
                .email(newUser.getEmail())
                .gender(newUser.getGender())
                .phone(newUser.getPhone())
                .build();
        userRepository.save(userEntity);
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
}

