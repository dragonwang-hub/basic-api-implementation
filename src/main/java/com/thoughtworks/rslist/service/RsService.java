package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.userrepository.RsEventRepository;
import com.thoughtworks.rslist.userrepository.UserRepository;
import com.thoughtworks.rslist.userrepository.VoteRepository;
import org.springframework.stereotype.Service;

@Service
public class RsService {
    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;

    public RsService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
    }

    public void addRsEvent(RsEvent rsEvent){
        UserEntity user = userRepository.findById(rsEvent.getUserId()).get();
        RsEventEntity responseEntity = RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .user(user)
                .build();
        rsEventRepository.save(responseEntity);
    }
}
