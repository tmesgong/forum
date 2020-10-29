package com.company.global;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.Key;

@Configuration

@PropertySource(value = {"classpath:email-format.properties","classpath:key.properties", "classpath:front-end.properties"}, encoding = "utf-8")
public class AppConfig implements WebMvcConfigurer {
    @Bean
    public Key key(@Value("${com.company.forum.key}") String key){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }
    @Bean
    public Key refreshKey(@Value("${com.company.forum.refresh-key}") String key){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }
    @Bean
    public Key resetKey(@Value("${com.company.forum.reset-key}") String key){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));

    }
    @Bean
    public AuthInterceptor authInterceptor(){
        return new AuthInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor()).addPathPatterns("/**");
    }
}
