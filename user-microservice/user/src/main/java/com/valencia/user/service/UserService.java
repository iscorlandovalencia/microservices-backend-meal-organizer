package com.valencia.user.service;

import com.valencia.user.controller.UserController;
import com.valencia.user.entity.User;
import com.valencia.user.exception.ResourceNotFoundException;
import com.valencia.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        log.info("Get All users");
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) throws Exception{
        log.info("Get user : " + userId);
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found for this id :: " + userId)));
        return user;
    }

    public User updateUser(Long userId, User fromUser) throws Exception {
        log.info("Looking for user : " + userId);
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId)));

        User thisUser = new User();
        if (user.isPresent()) {
            log.info("user is present? : " + (user.isPresent() ? "YES" : "NO"));
            thisUser = user.get();
        }

        thisUser.setName(fromUser.getName());
        thisUser.setLastname(fromUser.getLastname());
        thisUser.setEmail(fromUser.getEmail());
        final User updatedUser = userRepository.save(thisUser);
        log.info("User updated");
        return updatedUser;
    }

    public Map< String, Boolean > deleteUser(Long userId) throws Exception {
        log.info("Looking for user : " + userId);
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId)));

        userRepository.delete(user.get());
        Map < String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        log.info("user deleted : " + userId);
        return response;
    }

    public User resetPassword(Long userId, User fromUser) throws Exception {
        log.info("Looking for user : " + userId);
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId)));

        User thisUser = new User();
        if(user.isPresent()){
            log.info("user is present? : " + (user.isPresent() ? "YES" : "NO"));
            thisUser = user.get();
        }

        thisUser.setPassword(fromUser.getPassword());
        final User updatedUser = userRepository.save(thisUser);
        log.info("user updated : " + updatedUser.getId());
        return updatedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
