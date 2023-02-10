package com.security.mysec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.security.mysec.entity.UserEntity;
import com.security.mysec.repo.UserRepository;

@Controller
public class IndexController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/")
    public String index(){

        return "home";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }
    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }
    @GetMapping("/user")
    public String user(){
        return "user";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }
    @PostMapping("/signup")
    public String doSignup(String username, String password){
        
        UserEntity user = UserEntity.builder()
            .name(username)
            .password(passwordEncoder.encode(password))
            .role("user")
            .build();
        userRepository.save(user);

        return "redirect:/login";
    }
    // @GetMapping("/logout")
    // public String logout(){
    //     return "join";
    // }

    // @PostMapping("/login")
    // public @ResponseBody String doLogin(){
    //     return "login!!";
    // }
    
}
