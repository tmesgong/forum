package com.company.global;

import com.company.exception.AuthException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Set;

public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    @Qualifier("key")
    private Key key;
    @Autowired
    @Qualifier("refreshKey")
    public Key refreshKey;
    @Autowired
    @Qualifier("resetKey")
    public Key resetKey;

    public Set<String> skipRequest = Set.of("POST/code",
            "POST/session",
            "POST/user",
            "POST/email-category/forget-password"
    );


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (skipRequest.contains(request.getMethod() + request.getRequestURI())) {
            return true;
        }

        String authentication = request.getHeader("Authorization");

        if (authentication == null || !authentication.startsWith("Bearer ")) {
            throw new AuthException("未登录或登陆错误,请重新登录");
        }
        String compactJws = authentication.substring("Bearer ".length());
        String userId;
        try {
            userId = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(compactJws).getBody().getSubject();
//            System.out.println(userId);
        } catch (Exception e) {

            if (dealWithSpecialRequest(request, compactJws))
                return true;
            else
                throw new AuthException("未登录或登陆错误,请重新登录");
        }
//        System.out.println("userId userId-------------------------"+userId);
        request.setAttribute("userId", userId);
        return true;
    }

    public boolean dealWithSpecialRequest(HttpServletRequest request, String compactJws) throws AuthException {
        if ("PUT/user".equals(request.getMethod() + request.getRequestURI())) {//忘记密码放行
            String userId;
            try {
                userId = Jwts.parserBuilder().setSigningKey(resetKey).build().parseClaimsJws(compactJws).getBody().getSubject();
                request.setAttribute("userId", userId);
                System.out.println(userId);
                return true;
            } catch (Exception e) {
                throw new AuthException("非法的请求");
            }
        } else if ("PUT/session".equals(request.getMethod() + request.getRequestURI())) {//刷新token放行
            String userId;
            try {
                userId = Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(compactJws).getBody().getSubject();
                request.setAttribute("userId", userId);
                return true;
            } catch (Exception e) {
                throw new AuthException("非法的请求");
            }
        }
        return false;
    }

}
