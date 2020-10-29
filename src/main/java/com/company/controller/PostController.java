package com.company.controller;

import com.company.domain.Post;
import com.company.domain.Reply;
import com.company.exception.AuthException;
import com.company.exception.PostException;
import com.company.exception.UserException;
import com.company.service.PostService;
import com.company.service.ReplyService;
import com.company.util.AssemblyException;
import com.company.util.ExceptionAssembly;
import com.company.util.Msg;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final ReplyService replyService;


    public PostController(PostService postService, ReplyService replyService) {
        this.postService = postService;
        this.replyService = replyService;
    }

    @PostMapping
    public Msg create(@RequestBody Post post, @RequestAttribute("userId")String userId) throws AssemblyException, AuthException {
        new ExceptionAssembly().executeAll(
                ()->{
                    if (post.getTitle() == null || post.getTitle().isBlank()){
                        throw new Exception("title:帖子标题不能为空");
                    }
                    if (post.getTitle().length() < 6 || post.getTitle().length() > 15){
                        throw new Exception("title:帖子标题的长度在6~15之间");
                    }
                },
                ()->{
                    if (post.getContent() == null || post.getContent().isBlank()){
                        throw new Exception("content:帖子内容不能为空");
                    }
                    if (post.getContent().length() < 15 || post.getContent().length() > 150){
                        throw new Exception("content:帖子内容的长度在15~150之间");
                    }
                }
        ).throwIfNotEmpty();
        post.setId(null);
        post.setDigest(post.getContent().substring(0, 20));
        post.setCreateDate(LocalDateTime.now());
        post.setLastReplyDate(post.getCreateDate());
        try {
            postService.createPost(post,userId);
        } catch (UserException userException) {
            throw new AuthException(userException.getMessage());
        }

        return Msg.newSuccessMsg();
    }


    @PutMapping("/{pid}")
    public Msg addReply(@RequestBody Reply reply, @PathVariable("pid") String pid, @RequestAttribute("userId")String userId) throws AuthException, AssemblyException {
        new ExceptionAssembly().executeAll(
                ()->{
                    if (reply.getContent() == null || reply.getContent().isBlank()) {
                        throw new Exception("content:回复内容不能为空");
                    }
                    if (reply.getContent().length() < 15 || reply.getContent().length() > 50 ){
                        throw new Exception("content:回复内容的长度在15~50之间");

                    }
                }
        ).throwIfNotEmpty();
        reply.setOrdinal(null);
        reply.setWriter(null);
        reply.setCreateDate(LocalDateTime.now());
        try {
            postService.addReply(reply, pid, userId);
        } catch (UserException | PostException e) {
            throw new AuthException(e.getMessage());
        }
        return Msg.newSuccessMsg();
    }




    @GetMapping
    public Msg listPosts(@RequestParam(name = "page", defaultValue = "0")Integer page){
        return Msg.newSuccessMsg(postService.listPosts(page));
    }

    @GetMapping("/{pid}")
    public Msg listPost(@PathVariable("pid") String pid, @RequestAttribute("userId")String uid) throws AuthException {

        try {
            return Msg.newSuccessMsg(postService.listPost(pid,uid));
        } catch (PostException | UserException e) {
            throw new AuthException(e.getMessage());
        }
    }

    @GetMapping("/{pid}/reply")
    public Msg listReplies(@PathVariable("pid") String pid, @RequestParam(name = "page", defaultValue = "0")Integer page){
        return Msg.newSuccessMsg(replyService.listReplies(pid,page));

    }

    @GetMapping("/all/user")
    public Msg listUserPost(@RequestAttribute("userId")String userId,@RequestParam(name = "page", defaultValue = "0")Integer page){
        return Msg.newSuccessMsg(postService.listPost(userId, page));
    }

}
