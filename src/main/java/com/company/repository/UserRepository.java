package com.company.repository;

import com.company.domain.User;
import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User, String>,CustomizedUserRepository {
    User findByEmail(String email);
    User findById(String id);
    void save(User user);
    boolean existsByEmail(String email);





}
