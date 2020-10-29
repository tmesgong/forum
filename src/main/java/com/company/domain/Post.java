package com.company.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.TextCriteria;

import java.time.LocalDateTime;
import java.util.List;
@Document
public class Post {
    @Id
    private String id;
    private String title;
    private String digest;
    private String content;
    private LocalDateTime createDate;
    private User writer;
    private LocalDateTime lastReplyDate;

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", digest='" + digest + '\'' +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                ", writer=" + writer +
                ", lastReplyDate=" + lastReplyDate +
                '}';
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }


    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public LocalDateTime getLastReplyDate() {
        return lastReplyDate;
    }

    public void setLastReplyDate(LocalDateTime lastReplyDate) {
        this.lastReplyDate = lastReplyDate;
    }
}
