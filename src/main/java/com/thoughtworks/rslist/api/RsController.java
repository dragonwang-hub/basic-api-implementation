package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
    private List<String> rsList = initRsList();
    private List<String> initRsList() {
        List<String> tempRsList = new ArrayList<>();
        tempRsList.add("第一条事件");
        tempRsList.add("第二条事件");
        tempRsList.add("第三条事件");
        return tempRsList;
    }

    @GetMapping("/rs/list")
    public String getAllRsEvent(@RequestParam(required = false) Integer start,
                                @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return rsList.toString();
        }
        return rsList.subList(start - 1, end).toString();

    }

    @GetMapping("/rs/{index}")
    public String getRsEvent(@PathVariable int index) {
        return rsList.get(index - 1);
    }


    @PostMapping("/rs/event")
    public void addRsEvent(@RequestBody String rsEventString) {
        rsList.add(rsEventString);
    }


}
