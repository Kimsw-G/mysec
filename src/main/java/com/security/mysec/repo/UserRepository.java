package com.security.mysec.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.mysec.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Long>{
    Optional<UserEntity> findByName(String name);    
}
