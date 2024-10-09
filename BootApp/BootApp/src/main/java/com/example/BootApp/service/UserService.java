package com.example.BootApp.service;



import com.example.BootApp.model.User;
import com.example.BootApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }
    public void saveUser (User user) {
        userRepository.save(user);
    }
    public void deleteById (long id) {
        userRepository.deleteById(id);
    }
}
