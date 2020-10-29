package com.company.dto;

import com.company.domain.User;

public class UserWithoutPassword {
    private User user;

    public UserWithoutPassword(User user) {
        this.user = user;
    }

    public String getId() {
        return user.getId();
    }

    public String getName() {
        return user.getName();
    }

    public String getEmail() {
        return user.getEmail();
    }
}
