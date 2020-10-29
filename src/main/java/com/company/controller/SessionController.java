package com.company.controller;

import com.company.domain.User;
import com.company.dto.UserWithoutPassword;
import com.company.exception.AuthException;
import com.company.exception.UserException;
import com.company.service.UserService;
import com.company.util.AssemblyException;
import com.company.util.ExceptionAssembly;
import com.company.util.Msg;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/session")
public class SessionController {
    private final UserService userService;
    private final Key key;
    private final Key refreshKey;


    public SessionController(UserService userService, @Qualifier("key") Key key, @Qualifier("refreshKey") Key refreshKey) {
        this.userService = userService;
        this.key = key;
        this.refreshKey = refreshKey;
    }

    @PostMapping
    public Msg create(@RequestBody User user) throws AssemblyException {
        new ExceptionAssembly().executeAll(() -> {
                    if (user.getPassword() == null || user.getPassword().isBlank()) {
                        throw new Exception("password:密码不能为空");
                    }
                    if (user.getPassword().length() < 6 || user.getPassword().length() > 15) {
                        throw new Exception("password:密码的长度在6~15之间");
                    }
                },
                () -> {
                    if (user.getEmail() == null || user.getEmail().isBlank()) {
                        throw new Exception("email:邮箱不能为空");
                    }
                    if (!user.getEmail().matches("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$")) {
                        throw new Exception("email:邮箱格式格式不正确");
                    }
                }
        ).throwIfNotEmpty();
        UserWithoutPassword userInfo;

        try {
            userInfo = userService.login(user);
        } catch (UserException userException) {
            throw AssemblyException.singleton("user", userException.getMessage());
        }
//        System.out.println(userInfo.getId());
        String token = Jwts.builder()
                .setSubject(userInfo.getId())
                .setExpiration(
                        Date.from(Instant.now().plus(2, ChronoUnit.HOURS))
                )
                .signWith(key)
                .compact();
        String refreshToken = Jwts.builder()
                .setSubject(userInfo.getId())
                .setExpiration(
                        Date.from(Instant.now().plus(4, ChronoUnit.HOURS))
                )
                .signWith(refreshKey)
                .compact();

        return Msg.newSuccessMsg(
                Map.of(
                        "token", token,
                        "refresh-token", refreshToken
                )
        );
    }


    @PutMapping
    public Msg refresh(@RequestAttribute("userId") String userId) {
        String tokenNew = Jwts.builder()
                .setSubject(userId)
                .setExpiration(
                        Date.from(Instant.now().plus(2, ChronoUnit.HOURS))
                )
                .signWith(key)
                .compact();
        String refreshTokenNew = Jwts.builder()
                .setSubject(userId)
                .setExpiration(
                        Date.from(Instant.now().plus(4, ChronoUnit.HOURS))
                )
                .signWith(refreshKey)
                .compact();

        return Msg.newSuccessMsg(
                Map.of(
                        "token", tokenNew,
                        "refresh-token", refreshTokenNew
                )
        );

    }

}
