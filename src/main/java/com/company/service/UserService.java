package com.company.service;

import com.company.domain.User;
import com.company.dto.UserWithoutPassword;
import com.company.exception.UserException;
import com.company.repository.UserRepository;
import com.company.util.EmailServer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final String emailFormat;
    private final String resetEmailFormat;
    private final EmailServer emailServer;

    public UserService(StringRedisTemplate redisTemplate,
                       @Value("${com.company.forum.email.format}") String emailFormat,
                       EmailServer emailServer,
                       UserRepository userRepository,
                       @Value("${com.company.forum.email.reset-email.format}") String resetEmailFormat) {
        this.redisTemplate = redisTemplate;
        this.emailFormat = emailFormat;
        this.emailServer = emailServer;
        this.userRepository = userRepository;
        this.resetEmailFormat = resetEmailFormat;
    }

    public void sendVerificationCode(String email) throws MessagingException, UserException {
        //若邮箱已被注册，不发送邮件
        if(userRepository.existsByEmail(email)){
            throw new UserException("邮箱已被注册");
        }
        String verificationCode = UUID.randomUUID().toString();
        //发送验证码邮件
        emailServer.sendTo(email,String.format(emailFormat, verificationCode));
        //将验证码放入redis
        redisTemplate.opsForValue().set(email, verificationCode, 5, TimeUnit.MINUTES);
    }
    @Transactional
    public void register(User user, String verificationCode, String codeKey) throws UserException {
        String code = redisTemplate.opsForValue().get(codeKey);
        if (!verificationCode.equals(code)){
            throw new UserException("验证码错误或失效");
        }
        redisTemplate.delete(codeKey);
        userRepository.save(user);
    }

    public UserWithoutPassword login(User user) throws UserException {
        User userFind = userRepository.findByEmail(user.getEmail());
        if (userFind == null || !userFind.getPassword().equals(user.getPassword())){
            throw new UserException("邮箱或密码错误");
        }
        return new UserWithoutPassword(userFind);
    }

    public String findUserId(String email) throws UserException {
        User userFind = userRepository.findByEmail(email);
        if(userFind == null){
            throw new UserException("邮箱尚未被注册");
        }
        return userFind.getId();
    }

    public void setResetPasswordEmail(String email, String urlWithToken) throws MessagingException {
        emailServer.sendTo(email,String.format(resetEmailFormat,urlWithToken));
    }



    public void updateUser(User user){
       userRepository.update(user);
    }


    public UserWithoutPassword showUserInfo(String uid){
        User user = userRepository.findById(uid);

        return new UserWithoutPassword(user);

    }


}
