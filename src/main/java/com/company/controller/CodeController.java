package com.company.controller;

import com.company.exception.UserException;
import com.company.service.UserService;
import com.company.util.AssemblyException;
import com.company.util.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/code")
public class CodeController {
    private final UserService userService;

//    private final Logger logger = LoggerFactory.getLogger(CodeController.class);

    public CodeController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Msg sendVerificationCode(@RequestBody String email) throws MessagingException, AssemblyException {

        if (email == null ||email.isBlank()){
            throw AssemblyException.singleton("email", "邮箱不能为空");

        }
        if (!email.matches("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$")){
            throw AssemblyException.singleton("email", ":邮箱格式格式不正确");

        }
        try {
            userService.sendVerificationCode(email);
        } catch (UserException userException) {
            throw AssemblyException.singleton("email", "邮箱已被注册");
        }
        return Msg.newSuccessMsg();
    }

}
