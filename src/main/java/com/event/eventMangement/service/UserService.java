package com.event.eventMangement.service;

import com.event.eventMangement.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createUser(User u);
    User getUserById(Long id);
}
