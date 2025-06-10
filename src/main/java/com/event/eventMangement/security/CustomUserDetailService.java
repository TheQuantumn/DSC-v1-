package com.event.eventMangement.security;

import com.event.eventMangement.entity.User;
import com.event.eventMangement.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepo.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getEmail())
                .password(u.getPass())
                .roles(u.getRoles().toArray(new String[0]))
                .build();

    }
}
