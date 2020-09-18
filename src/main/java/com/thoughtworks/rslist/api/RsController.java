package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.*;
import com.thoughtworks.rslist.userrepository.RsEventRepository;
import com.thoughtworks.rslist.userrepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class RsController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;


    @JsonView(RsEvent.Public.class)
    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getAllRsEvent(@RequestParam(required = false) Integer start,
                                                       @RequestParam(required = false) Integer end) throws MyIndexOutOfBoundsException {
        List<RsEventEntity> rsEntityList = rsEventRepository.findAll();
        List<RsEvent> rsList = rsEntityList.stream().map(rsEntity -> RsEvent.builder()
                .eventName(rsEntity.getEventName())
                .keyword(rsEntity.getKeyword())
                .rsEventId(rsEntity.getId())
                .rsVotes(rsEntity.getRsVotes())
                .build())
                .collect(Collectors.toList());
        if (start == null || end == null) {
            return ResponseEntity.ok(rsList);
        }
        if (start < 0 || start > rsList.size() || end < start || end > rsList.size()) {
            throw new MyIndexOutOfBoundsException("invalid request param");
        }

        return ResponseEntity.ok(rsList.subList(start - 1, end));
    }

    @JsonView(RsEvent.Public.class)
    @GetMapping("/rs/{rsEventId}")
    public ResponseEntity<RsEvent> getRsEvent(@PathVariable int rsEventId) throws IndexException {
        if (rsEventId < 0 || rsEventId > rsEventRepository.count()) {
            throw new IndexException("invalid index");
        }
        RsEventEntity rsEntity = rsEventRepository.findById(rsEventId).get();
        RsEvent rsEvent = RsEvent.builder()
                .eventName(rsEntity.getEventName())
                .keyword(rsEntity.getKeyword())
                .rsEventId(rsEntity.getId())
                .rsVotes(rsEntity.getRsVotes())
                .build();
        return ResponseEntity.ok(rsEvent);
    }

    @Transactional
    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent,BindingResult re) throws InvaildRsEventExcepttion {
        if (re.getAllErrors().size()!= 0) {
            throw new InvaildRsEventExcepttion("invalid param");
        }
        if (!userRepository.findById(rsEvent.getUserId()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        UserEntity user = userRepository.findById(rsEvent.getUserId()).get();
        RsEventEntity responseEntity = RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .user(user)
                .build();
        rsEventRepository.save(responseEntity);
        return ResponseEntity.created(null).build();
    }

    @Transactional
    @PatchMapping("/rs/{index}")
    public ResponseEntity<RsEvent> alterRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
        UserEntity user = userRepository.findById(rsEvent.getUserId()).get();
        RsEventEntity rsEventEntity = rsEventRepository.findById(index).get();
        if (!rsEventEntity.getUser().equals(user)) {
            return ResponseEntity.badRequest().build();
        }
        RsEventEntity responseEntity;
        if(rsEvent.getEventName().equals("")){
            responseEntity = RsEventEntity.builder()
                    .eventName(rsEventEntity.getEventName())
                    .keyword(rsEvent.getKeyword())
                    .user(user)
                    .build();
        }else if(rsEvent.getKeyword().equals("")){
            responseEntity = RsEventEntity.builder()
                    .eventName(rsEvent.getEventName())
                    .keyword(rsEventEntity.getKeyword())
                    .user(user)
                    .build();
        }else {
            responseEntity = RsEventEntity.builder()
                    .eventName(rsEvent.getEventName())
                    .keyword(rsEvent.getKeyword())
                    .user(user)
                    .build();
        }
        // 若是同一个user,则删除原有，增加新的
        rsEventRepository.delete(rsEventEntity);
        rsEventRepository.save(responseEntity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rs/{index}")
    public ResponseEntity deleteRsEvent(@PathVariable int index) {
        if(!rsEventRepository.findById(index).isPresent()){
            return ResponseEntity.badRequest().build();
        }
        rsEventRepository.deleteById(index);
        return ResponseEntity.ok().build();
    }
}
