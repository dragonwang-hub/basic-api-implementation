package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.exception.IndexException;
import com.thoughtworks.rslist.userrepository.RsEventRepository;
import com.thoughtworks.rslist.userrepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


import static com.thoughtworks.rslist.api.UserController.userList;


@RestController
public class RsController {
    private List<RsEvent> rsList = initRsList();

    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    private List<RsEvent> initRsList() {
        List<RsEvent> tempRsList = new ArrayList<>();
        User user = new User("dragon", 24, "male", "ylw@tw.com", "18812345678");
        tempRsList.add(new RsEvent("第一条事件", "无分类", user));
        tempRsList.add(new RsEvent("第二条事件", "无分类"));
        tempRsList.add(new RsEvent("第三条事件", "无分类"));
        return tempRsList;
    }

    @JsonView(RsEvent.Public.class)
    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getAllRsEvent(@RequestParam(required = false) Integer start,
                                                       @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return ResponseEntity.ok(rsList);
        }
        if (start < 0 || start > rsList.size() || end < start || end > rsList.size()) {
            throw new IndexOutOfBoundsException();
        }

        return ResponseEntity.ok(rsList.subList(start - 1, end));
    }

    @JsonView(RsEvent.Public.class)
    @GetMapping("/rs/{index}")
    public ResponseEntity<RsEvent> getRsEvent(@PathVariable int index) throws IndexException {
        if (index < 0 || index > rsList.size()) {
            throw new IndexException();
        }
        return ResponseEntity.ok(rsList.get(index - 1));
    }

    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent) throws JsonProcessingException {
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
        // 原代码，对内存数据操作
//        int index = -1;
//        if(userList.stream().anyMatch(user -> rsEvent.getUser().getUserName().equals(user.getUserName()))){
//            rsEvent.setUser(null);
//            rsList.add(rsEvent);
//            index = rsList.size();
//            return ResponseEntity.created(URI.create("/rs/" + index)).build();
//        }
//        userController.registerUser(rsEvent.getUser());
//        rsList.add(rsEvent);
//        index = rsList.size();
//        return ResponseEntity.created(URI.create("/rs/" + index)).build();
    }

    @PutMapping("/rs/{index}")
    public ResponseEntity<RsEvent> alterRsEvent(@PathVariable int index, @Valid @RequestBody RsEvent rsEvent) {
        UserEntity user = userRepository.findById(rsEvent.getUserId()).get();
        RsEventEntity rsEventEntity = rsEventRepository.findById(index).get();
        if (!rsEventEntity.getUser().equals(user)) {
            return ResponseEntity.badRequest().build();
        }

        RsEventEntity responseEntity = RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .user(user)
                .build();
        // 若是同一个user,则删除原有，增加新的
        rsEventRepository.delete(rsEventEntity);
        rsEventRepository.save(responseEntity);
        return ResponseEntity.ok().build();

//        rsList.get(index - 1).setKeyword(rsEvent.getKeyword());
//        rsList.get(index - 1).setEventName(rsEvent.getEventName());
//        return ResponseEntity.ok(rsList.get(index - 1));
    }

    @DeleteMapping("/rs/{index}")
    public ResponseEntity<RsEvent> deleteRsEvent(@PathVariable int index) {
        return ResponseEntity.ok(rsList.remove(index - 1));
    }

    @ExceptionHandler({IndexOutOfBoundsException.class, IndexException.class, MethodArgumentNotValidException.class})
    public ResponseEntity handleIndexOutOfBoundsException(Exception ex) throws JsonProcessingException {
        CommentError commentError = new CommentError();
        if (ex instanceof IndexOutOfBoundsException) {
            commentError.setError("invalid request param");
        } else if (ex instanceof IndexException) {
            commentError.setError("invalid index");
        } else if (ex instanceof MethodArgumentNotValidException) {
            commentError.setError("invalid param");
        }
        return ResponseEntity.badRequest().body(commentError);
    }
}
