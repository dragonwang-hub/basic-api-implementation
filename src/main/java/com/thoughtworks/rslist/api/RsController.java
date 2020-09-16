package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = initRsList();

    private List<RsEvent> initRsList() {
        List<RsEvent> tempRsList = new ArrayList<>();
        User user = new User("dragon", 24, "male", "ylw@tw.com", "18812345678");
        tempRsList.add(new RsEvent("第一条事件", "无分类", user));
        tempRsList.add(new RsEvent("第二条事件", "无分类", user));
        tempRsList.add(new RsEvent("第三条事件", "无分类", user));
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
        rsList.add(rsEvent);
    }

    @PutMapping("/rs/{index}")
    public RsEvent alterRsEvent(@PathVariable int index,@Valid @RequestBody RsEvent rsEvent) throws JsonProcessingException {
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
