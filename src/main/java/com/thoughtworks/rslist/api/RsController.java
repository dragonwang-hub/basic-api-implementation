package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        tempRsList.add(new RsEvent("第一条事件", "无分类"));
        tempRsList.add(new RsEvent("第二条事件", "无分类"));
        tempRsList.add(new RsEvent("第三条事件", "无分类"));
        return tempRsList;
    }

    @GetMapping("/rs/list")
    public List<RsEvent> getAllRsEvent(@RequestParam(required = false) Integer start,
                                       @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return rsList;
        }
        return rsList.subList(start - 1, end);
    }

    @GetMapping("/rs/{index}")
    public RsEvent getRsEvent(@PathVariable int index) {
        return rsList.get(index - 1);
    }

    @PostMapping("/rs/event")
    public void addRsEvent(@Valid @RequestBody RsEvent rsEvent) throws JsonProcessingException {
        for (User user : userList) {
            if (rsEvent.getUser().getUserName().equals(user.getUserName())) {
                rsEvent.setUser(null);
                rsList.add(rsEvent);
                return;
            }
        }
        userController.registerUser(rsEvent.getUser());
        rsList.add(rsEvent);
    }

    @PutMapping("/rs/{index}")
    public RsEvent alterRsEvent(@PathVariable int index, @Valid @RequestBody RsEvent rsEvent) throws JsonProcessingException {
        if (rsEvent.getEventName() == "") {
            rsList.get(index - 1).setKeyword(rsEvent.getKeyword());
        } else if (rsEvent.getKeyword() == "") {
            rsList.get(index - 1).setEventName(rsEvent.getEventName());
        } else {
            rsList.get(index - 1).setKeyword(rsEvent.getKeyword());
            rsList.get(index - 1).setEventName(rsEvent.getEventName());
        }
        return rsList.get(index - 1);
    }

    @DeleteMapping("/rs/{index}")
    public RsEvent deleteRsEvent(@PathVariable int index) {
        return rsList.remove(index - 1);
    }
}
