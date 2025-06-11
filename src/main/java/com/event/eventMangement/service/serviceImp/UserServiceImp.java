package com.event.eventMangement.service.serviceImp;

import com.event.eventMangement.entity.User;
import com.event.eventMangement.repository.UserRepo;
import com.event.eventMangement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User u) {
        if(userRepo.existsByEmail(u.getEmail())){
            throw new RuntimeException("User Already Exists");
        }
        u.setRoles(Arrays.asList("ADMIN"));
        u.setPass(passwordEncoder.encode(u.getPass()));
           return userRepo.save(u);

    }

    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(()->new RuntimeException("User 404"));
    }
}
