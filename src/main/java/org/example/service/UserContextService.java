package org.example.service;

import org.example.context.UserContext;
import org.example.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {

    public void setCurrentUser(User user) {
        UserContext.setCurrentUser(user);
    }

    public User getCurrentUser() {
        return UserContext.getCurrentUser();
    }

    public void clearCurrentUser() {
        UserContext.clear();
    }
}