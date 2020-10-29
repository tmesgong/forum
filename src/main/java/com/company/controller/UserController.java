package com.company.controller;

import com.company.domain.User;
import com.company.dto.UserWithVerificationCode;
import com.company.exception.AuthException;
import com.company.exception.UserException;
import com.company.service.UserService;
import com.company.util.AssemblyException;
import com.company.util.ExceptionAssembly;
import com.company.util.Msg;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public Msg register(@RequestBody UserWithVerificationCode userWithVerificationCode) throws AssemblyException {
        User user = userWithVerificationCode.getUser();
        String verificationCode = userWithVerificationCode.getVerificationCode();
        new ExceptionAssembly().executeAll(
                () -> {
                    if (user.getName() == null || user.getName().isBlank()) {
                        throw new Exception("name:用户名不能为空");
                    }
                    if (user.getName().length() < 3 || user.getName().length() > 12) {
                        throw new Exception("name:用户名的长度在3~12之间");
                    }
                },
                () -> {
                    if (user.getPassword() == null || user.getPassword().isBlank()) {
                        throw new Exception("password:密码不能为空");
                    }
                    if (user.getPassword().length() < 6 || user.getPassword().length() > 15){
                        throw new Exception("password:密码的长度在6~15之间");
                    }
                },
                ()->{
                    if (user.getEmail() == null || user.getEmail().isBlank()){
                        throw new Exception("email:邮箱不能为空");
                    }
                    if (!user.getEmail().matches("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$")){
                        throw new Exception("email:邮箱格式格式不正确");
                    }
                },
                () ->{
                    if (verificationCode == null || verificationCode.isBlank()){
                        throw new Exception("verificationCode:验证码不能为空");

                    }
                }
        ).throwIfNotEmpty();
        user.setId(null);
        try {
            userService.register(user,verificationCode,user.getEmail());
        } catch (UserException userException) {
            throw AssemblyException.singleton("verificationCode", userException.getMessage());
        }
        return Msg.newSuccessMsg();
    }


    @PutMapping
    public Msg update(@RequestBody User user, @RequestAttribute("userId")String userId) throws AuthException, AssemblyException {
        if (user.getId() != null && !userId.equals(user.getId())){
            throw new AuthException("非法操作");
        }
        user.setId(userId);
        new ExceptionAssembly().executeAll(
                () -> {
                    if (user.getName() == null) return;
                    if (user.getName().isBlank()) {
                        throw new Exception("name:用户名不能为空");
                    }
                    if (user.getName().length() < 3 || user.getName().length() > 12) {
                        throw new Exception("name:用户名的长度在3~12之间");
                    }
                },
                () -> {
                    if (user.getPassword() == null) return;
                    if (user.getPassword().isBlank()) {
                        throw new Exception("password:密码不能为空");
                    }

                    if (user.getPassword().length() < 6 || user.getPassword().length() > 15){
                        throw new Exception("password:密码的长度在6~15之间");
                    }
                },
                ()->{
                    if (user.getEmail() == null) return;
                    if (user.getEmail().isBlank()){
                        throw new Exception("email:邮箱不能为空");
                    }
                    if (!user.getEmail().matches("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$")){
                        throw new Exception("email:邮箱格式格式不正确");
                    }
                }
        ).throwIfNotEmpty();
       userService.updateUser(user);
        return Msg.newSuccessMsg();
    }


    @GetMapping
    public Msg showUserInfo(@RequestAttribute("userId")String uid){
        return Msg.newSuccessMsg(userService.showUserInfo(uid));
    }

}
