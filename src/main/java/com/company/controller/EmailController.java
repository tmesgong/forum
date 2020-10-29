package com.company.controller;

import com.company.exception.UserException;
import com.company.service.UserService;
import com.company.util.AssemblyException;
import com.company.util.ExceptionAssembly;
import com.company.util.Msg;
import io.jsonwebtoken.Jwts;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import java.net.URL;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/email-category")
public class EmailController {
    private final UserService userService;
    private final Key resetKey;


    public EmailController(UserService userService, @Qualifier("resetKey") Key resetKey) {
        this.userService = userService;
        this.resetKey = resetKey;
    }

    @PostMapping("/forget-password")
    public Msg sendResetEmail(@RequestBody Map<String, String> requestMap) throws AssemblyException, MessagingException {
        String email = requestMap.get("email");
        String url = requestMap.get("front-end-url");
        new ExceptionAssembly().executeAll(
                ()->{
                    if (email == null ||email.isBlank()){
                        throw new Exception("email:邮箱不能为空");

                    }
                    if (!email.matches("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$")){
                        throw new Exception("email:邮箱格式格式不正确");

                    }
                },
                ()->{
                    if (url == null || url.isBlank()){
                        throw new Exception("front-end-url:url不能为空");
                    }
                }
        );
        String userId;
        try {
            userId = userService.findUserId(email);
            System.out.println(userId);
        } catch (UserException userException) {
            throw AssemblyException.singleton("email", userException.getMessage());
        }
        String token = Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(
                        Date.from(Instant.now().plus(30, ChronoUnit.MINUTES))
                )
                .signWith(resetKey)
                .compact();
       String urlWithToken = UriComponentsBuilder.fromUriString(url)
               .queryParam("token","{token}").build(token).toString();
        userService.setResetPasswordEmail(email,urlWithToken);
        return Msg.newSuccessMsg();
    }

}
