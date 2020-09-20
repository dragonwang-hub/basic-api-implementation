package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


import static com.thoughtworks.rslist.api.UserController.userList;


@RestController
public class RsController {
    private List<RsEvent> rsList = initRsList();

    @Autowired
    UserController userController;

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
        return ResponseEntity.ok(rsList.subList(start - 1, end));
    }

    @JsonView(RsEvent.Public.class)
    @GetMapping("/rs/{index}")
    public ResponseEntity<RsEvent> getRsEvent(@PathVariable int index) {
        return ResponseEntity.ok(rsList.get(index - 1));
    }

    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent) throws JsonProcessingException {
        int index = -1;
        for (User user : userList) {
            if (rsEvent.getUser().getUserName().equals(user.getUserName())) {
                rsEvent.setUser(null);
                rsList.add(rsEvent);
                index = rsList.size();
                return ResponseEntity.status(201).header("index",String.valueOf(index)).build();
            }
        }
        userController.registerUser(rsEvent.getUser());
        rsList.add(rsEvent);
        index = rsList.size();
        return ResponseEntity.status(201).header("index",String.valueOf(index)).build();
    }

    @PutMapping("/rs/{index}")
    public ResponseEntity<RsEvent> alterRsEvent(@PathVariable int index, @Valid @RequestBody RsEvent rsEvent) {
        rsList.get(index - 1).setKeyword(rsEvent.getKeyword());
        rsList.get(index - 1).setEventName(rsEvent.getEventName());
        return ResponseEntity.ok(rsList.get(index - 1));
    }

    @DeleteMapping("/rs/{index}")
    public ResponseEntity<RsEvent> deleteRsEvent(@PathVariable int index) {
        return ResponseEntity.ok(rsList.remove(index - 1));
    }
}
