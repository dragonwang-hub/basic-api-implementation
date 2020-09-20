package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.userrepository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(User newUser){
        UserEntity userEntity = UserEntity.builder()
                .userName(newUser.getUserName())
                .age(newUser.getAge())
                .email(newUser.getEmail())
                .gender(newUser.getGender())
                .phone(newUser.getPhone())
                .build();
        userRepository.save(userEntity);
    }
}
