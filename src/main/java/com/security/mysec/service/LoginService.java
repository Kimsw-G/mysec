package com.security.mysec.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.mysec.entity.UserEntity;
import com.security.mysec.repo.UserRepository;

@Service    
public class LoginService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{
        System.out.println("logining...");
        UserEntity user = userRepository.findByName(username).orElseThrow(()->new IllegalArgumentException("no user!!"));
        return new User(user.getName(),user.getPassword(),Arrays.asList(new SimpleGrantedAuthority(user.getRole())));
    }
}
