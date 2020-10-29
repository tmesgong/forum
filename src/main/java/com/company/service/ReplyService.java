package com.company.service;

import com.company.domain.Reply;
import com.company.repository.ReplyRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {
    private final ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }


    public Page<Reply> listReplies(String pid, Integer page){
        return replyRepository.findByPostIdOrderByCreateDate(pid, PageRequest.of(page, 10));

    }
}
