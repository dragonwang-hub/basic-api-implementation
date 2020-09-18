package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.userrepository.RsEventRepository;
import com.thoughtworks.rslist.userrepository.UserRepository;
import com.thoughtworks.rslist.userrepository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
public class VoteController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    @Transactional
    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity addVotes(@PathVariable int rsEventId, @RequestBody Vote vote) {
        UserEntity userEntity = userRepository.findById(vote.getUserId()).get();
        RsEventEntity rsEventEntity = rsEventRepository.findById(rsEventId).get();
        int restVotes = userEntity.getVoteNumb() - vote.getVoteNumb();
        if (restVotes >= 0) {
            VoteEntity voteEntity = VoteEntity.builder()
                    .voteNumb(vote.getVoteNumb())
                    .user(userEntity)
                    .curTime(vote.getCurTime())
                    .rsevent(rsEventEntity)
                    .build();
            userEntity.setVoteNumb(restVotes);
            voteRepository.save(voteEntity);
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }
}


