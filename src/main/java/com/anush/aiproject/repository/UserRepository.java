package com.anush.aiproject.repository;

import java.util.Optional;

import com.anush.aiproject.entity.User;

public interface UserRepository extends AbstractRepository<User,Long>{

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    
} 