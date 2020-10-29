package com.company.controller;

import com.company.exception.AuthException;
import com.company.exception.HistoryException;
import com.company.service.HistoryService;
import com.company.util.Msg;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/history")
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public Msg listHistories(@RequestAttribute("userId")String userId, @RequestParam(name = "page", defaultValue = "0")Integer page){
        return Msg.newSuccessMsg(historyService.listHistories(userId,page));
    }

    @DeleteMapping("/{hid}")
    public Msg deleteHistory(@RequestAttribute("userId")String userId,@PathVariable("hid")String hid) throws AuthException {
        try {
            historyService.deleteHistory(userId,hid);
        } catch (HistoryException e) {
            throw new AuthException(e.getMessage());
        }
        return Msg.newSuccessMsg();
    }


}
