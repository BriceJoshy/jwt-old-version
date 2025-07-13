package com.example.jwt_old_version.config;

import com.example.jwt_old_version.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig{

    @Autowired
    private UserService userService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth-> auth
                        .antMatchers("/users/register", "/users/login").permitAll()
                        .antMatchers(HttpMethod.POST, "/books").hasAuthority("ADMIN")
                        .antMatchers(HttpMethod.PUT,"/books/**").hasAuthority("ADMIN")
                        .antMatchers(HttpMethod.DELETE,"/books/**").hasAuthority("ADMIN")
                        .antMatchers(HttpMethod.GET, "/books").hasAnyAuthority("USER", "ADMIN")
                        .antMatchers(HttpMethod.POST, "/books/*/borrow").hasAuthority("USER")
                        .antMatchers(HttpMethod.POST, "/books/*/return").hasAuthority("USER")
                        .antMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority("ADMIN", "USER")
                        .antMatchers(HttpMethod.PUT, "/users/**").hasAnyAuthority("ADMIN", "USER")
                ).httpBasic(Customizer.withDefaults())
                .build();
    }

    // ✅ Define AuthenticationManager bean
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}


