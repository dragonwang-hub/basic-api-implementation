package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.userrepository.RsEventRepository;
import com.thoughtworks.rslist.userrepository.UserRepository;
import com.thoughtworks.rslist.userrepository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    @Transactional
    @PostMapping("/rs/votes/{id}")
    public ResponseEntity addVotes(@PathVariable int id, @RequestBody Vote vote) {
        UserEntity userEntity = userRepository.findById(vote.getUserId()).get();
        RsEventEntity rsEventEntity = rsEventRepository.findById(id).get();
        int restVotes = userEntity.getVoteNumb() - vote.getVoteNumb();
        if (restVotes >= 0) {
            VoteEntity voteEntity = VoteEntity.builder()
                    .voteNumb(vote.getVoteNumb())
                    .user(userEntity)
                    .voteTime(vote.getVoteTime())
                    .rsEvent(rsEventEntity)
                    .build();
            userEntity.setVoteNumb(restVotes);
            voteRepository.save(voteEntity);
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/rs/votes/user&rsevent")
    public ResponseEntity<List<Vote>> getVotesByUserIdAndRsEventId(
            @RequestParam int userId,
            @RequestParam int rsEventId,
            @RequestParam(defaultValue = "1") int pageIndex) {
        int everyPageSize = 5;
        int peopleComputorGapAboutPageIndex = 1;
        Pageable pageable = PageRequest.of(pageIndex - peopleComputorGapAboutPageIndex, everyPageSize);
        List<VoteEntity> voteEntities = voteRepository.findAllByUserIdAndRsEventId(userId, rsEventId, pageable);
        List<Vote> votes = voteEntities.stream().map(voteEntity -> Vote.builder()
                .userId(voteEntity.getUser().getId())
                .rsEventId(voteEntity.getRsEvent().getId())
                .voteNumb(voteEntity.getVoteNumb())
                .voteTime(voteEntity.getVoteTime())
                .build()
        ).collect(Collectors.toList());
        return ResponseEntity.ok(votes);
    }

    @GetMapping("/rs/votes/{id}")
    public ResponseEntity<List<Vote>> getAllVotesOfSpecifyPage(@PathVariable int id) {
        int everyPageSize = 5;
        int peopleComputorGapAboutPageIndex = 1;
        int pageIndex = id;
        Pageable pageable = PageRequest.of(pageIndex - peopleComputorGapAboutPageIndex, everyPageSize);
        List<VoteEntity> voteEntities = voteRepository.findAll(pageable);
        List<Vote> votes = voteEntities.stream().map(voteEntity -> Vote.builder()
                .userId(voteEntity.getUser().getId())
                .rsEventId(voteEntity.getRsEvent().getId())
                .voteNumb(voteEntity.getVoteNumb())
                .voteTime(voteEntity.getVoteTime())
                .build()
        ).collect(Collectors.toList());
        return ResponseEntity.ok(votes);
    }

    @GetMapping("/rs/votes/timerange")
    public ResponseEntity<List<Vote>> getVotesByLocalDateTimeBetween(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        List<VoteEntity> voteEntities = voteRepository.findByVoteTimeBetween(start, end);
        List<Vote> votes = voteEntities.stream().map(voteEntity -> Vote.builder()
                .userId(voteEntity.getUser().getId())
                .rsEventId(voteEntity.getRsEvent().getId())
                .voteNumb(voteEntity.getVoteNumb())
                .voteTime(voteEntity.getVoteTime())
                .build()
        ).collect(Collectors.toList());
        return ResponseEntity.ok(votes);
    }
}



