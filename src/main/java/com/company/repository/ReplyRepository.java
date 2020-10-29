package com.company.repository;

import com.company.domain.Reply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface ReplyRepository extends Repository<Reply,String> {
    Reply save(Reply reply);
    Page<Reply> findByPostIdOrderByCreateDate(String pid, Pageable pageable);
}
