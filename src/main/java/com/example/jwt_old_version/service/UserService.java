package com.example.jwt_old_version.service;

import com.example.jwt_old_version.entity.User;
import com.example.jwt_old_version.repository.UserRepository;
import net.bytebuddy.build.Plugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("user not found");
        }
        SimpleGrantedAuthority authorities = new SimpleGrantedAuthority("ROLE_" + user.get().getRole());

        return new org.springframework.security.core.userdetails.User(
                user.get().getUsername(),
                user.get().getPassword(),
                List.of(authorities)
        );
    }

    public Optional<User> register(User user){
        if(!userRepository.findByUsername(user.getUsername()).isEmpty()){
            return Optional.empty();
        }
        System.out.println();
        return Optional.of(userRepository.save(user));
    }
}
