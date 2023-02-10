package com.security.mysec.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import com.security.mysec.config.handler.MyLoginSuccessHandler;
import com.security.mysec.config.handler.MyLogoutSuccessHandler;

@Configuration
// @EnableMethodSecurity
@EnableWebSecurity // spring security 필터가 spring 필터체인에 등록
public class SecurityConfig {
    private static final String[] AUTH_WHITELIST = {
        "/", "/login","/logout","/signup", "/home"
    };
    private static final String[] AUTH_USERLIST = {
        "/user"
    };
    private static final String[] AUTH_MANAGERLIST = {
        "/manager"
    };
    private static final String[] AUTH_ADMINLIST = {
        "/admin"
    };


    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        AuthorityAuthorizationManager<RequestAuthorizationContext> userAuth 
            = AuthorityAuthorizationManager.<RequestAuthorizationContext>hasRole("USER");
        userAuth.setRoleHierarchy(roleHierarchy());
        AuthorityAuthorizationManager<RequestAuthorizationContext> managerAuth 
            = AuthorityAuthorizationManager.<RequestAuthorizationContext>hasRole("MANAGER");
        managerAuth.setRoleHierarchy(roleHierarchy());
        AuthorityAuthorizationManager<RequestAuthorizationContext> adminAuth 
            = AuthorityAuthorizationManager.<RequestAuthorizationContext>hasRole("ADMIN");
        adminAuth.setRoleHierarchy(roleHierarchy());
        http.csrf().disable()
            // .httpBasic(Customizer.withDefaults());
            // .securityContext(
            //     securityContext -> securityContext
            //     .securityContextRepository(new NullSecurityContextRepository())
            //     .requireExplicitSave(true));
            // .formLogin(form -> form
            //     .loginPage("/login")
            // )
            .authorizeHttpRequests(authorize -> authorize
                // .anyRequest().permitAll()
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .requestMatchers(AUTH_USERLIST).access(userAuth)
                .requestMatchers(AUTH_MANAGERLIST).access(managerAuth)
                .requestMatchers(AUTH_ADMINLIST).access(adminAuth)
            )
            // .formLogin()
            .formLogin(form->form
                // .loginPage("/login")
                // .loginProcessingUrl("/login")
                .successHandler(new MyLoginSuccessHandler())
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(new MyLogoutSuccessHandler())
            )
            ;

        return http.build();
    }

    // @Bean
    // AccessDecisionVoter hierarchyVoter(){

    //     return new RoleHierarchyVoter(null);
    // }
    @Bean
    public RoleHierarchy roleHierarchy() {
        String role = "ROLE_ADMIN > ROLE_MANAGER > ROLE_USER";
        RoleHierarchyImpl r = new RoleHierarchyImpl();
        r.setHierarchy(role);
        return r;
    }



    // @Bean
    // public UserDetailsService userDetailsService(){
    //     UserDetails banana = User.builder()
    //         .username("banana")
    //         .password(passwordEncoder().encode("banana"))
    //         .roles("USER")
    //         .build();

    //     UserDetails furufuru = User.builder()
    //         .username("furufuru")
    //         .password(passwordEncoder().encode("furufuru"))
    //         .roles("MANAGER")
    //         .build();
        
    //     UserDetails admin = User.builder()
    //         .username("admin")
    //         .password(passwordEncoder().encode("admin"))
    //         .roles("ADMIN")
    //         .build();
    //     List<UserDetails> list = new ArrayList<UserDetails>();
    //     list.add(banana);
    //     list.add(furufuru);
    //     list.add(admin);

            
    //     return new InMemoryUserDetailsManager(list);
    // }
}
