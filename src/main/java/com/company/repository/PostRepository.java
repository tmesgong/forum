package com.company.repository;

import com.company.domain.Post;
import com.company.domain.Reply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

@org.springframework.stereotype.Repository
public interface PostRepository extends Repository<Post, String> {


    Post save(Post post);


    Post findById(String id);

    @Query(fields = "{'writer.password':0}")
    Page<Post> findAllByOrderByLastReplyDateDesc(Pageable pageable);

    @Query(fields = "{'writer.password':0}")
    Page<Post> findByWriterIdOrderByCreateDateDesc(String uid, Pageable pageable);








}
