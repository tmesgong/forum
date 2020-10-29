package com.company.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.LocalTime;
@Document
public class History {
    @Id
    private String id;
    @DBRef
    private User user;
    @DBRef
    private Post post;
    private LocalDateTime lastViewDate;
    private boolean deleted;

    public History(String id, User user, Post post, LocalDateTime lastViewDate, boolean deleted) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.lastViewDate = lastViewDate;
        this.deleted = deleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getLastViewDate() {
        return lastViewDate;
    }

    public void setLastViewDate(LocalDateTime lastViewDate) {
        this.lastViewDate = lastViewDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
