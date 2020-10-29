package com.company.service;

import com.company.domain.History;
import com.company.domain.Post;
import com.company.domain.Reply;
import com.company.domain.User;
import com.company.exception.PostException;
import com.company.exception.UserException;
import com.company.repository.HistoryRepository;
import com.company.repository.PostRepository;
import com.company.repository.ReplyRepository;
import com.company.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final HistoryRepository historyRepository;
    private final ReplyRepository replyRepository;

    public PostService(UserRepository userRepository, PostRepository postRepository, HistoryRepository historyRepository, ReplyRepository replyRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.historyRepository = historyRepository;
        this.replyRepository = replyRepository;
    }

    public void createPost(Post post, String id) throws UserException {
        User user = userRepository.findById(id);
        if (user == null) throw new UserException("用户不存在");
        post.setWriter(user);
        postRepository.save(post);
    }

    public void addReply(Reply reply, String pid, String uid) throws UserException, PostException {
        User user = userRepository.findById(uid);
        if (user == null) throw new UserException("用户不存在");
        Post post = postRepository.findById(pid);
        if (post == null) throw new PostException("帖子不存在");
        reply.setWriter(user);
        reply.setPost(post);
        post.setLastReplyDate(reply.getCreateDate());
        replyRepository.save(reply);
        postRepository.save(post);
    }
    public Page<Post> listPosts(int page){
        return postRepository.findAllByOrderByLastReplyDateDesc(PageRequest.of(page, 10));
    }
    public Post listPost(String pid, String uid) throws PostException, UserException {
        Post post = postRepository.findById(pid);
        if (post == null) throw new PostException("帖子不存在");
        User user = userRepository.findById(uid);
        if (user == null) throw new UserException("用户不存在");
        History history = new History(null,user,post, LocalDateTime.now(),false);
        historyRepository.save(history);
        post.getWriter().setPassword(null);
        return post;
    }

    public Page<Post> listPost(String uid, Integer page){

        return postRepository.findByWriterIdOrderByCreateDateDesc(uid,PageRequest.of(page,10));

    }
}
