package com.valencia.user.service;

import com.valencia.user.controller.UserController;
import com.valencia.user.entity.User;
import com.valencia.user.exception.ResourceNotFoundException;
import com.valencia.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public List<User> getAllUsers() {
        LOG.info("Get All users");
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) throws Exception{
        LOG.info("Get user : " + userId);
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found for this id :: " + userId)));
        return user;
    }

    public User createUser(User fromUser) throws Exception {
        User user = new User();
        user.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
        user.setName(fromUser.getName());
        user.setLastName(fromUser.getLastName());
        user.setEmail(fromUser.getEmail());
        user.setPassword(fromUser.getPassword());

        LOG.info("Saving a user : " + fromUser.getId());
        userRepository.save(user);
        return user;
    }

    public Boolean getLoginByEmail(String email, String password) throws Exception {
        Boolean existUser = false;
        LOG.info("Get email user : " + email);
        List<User> user = Optional.ofNullable(userRepository.findByEmailIs(email))
                //List<User> user = Optional.ofNullable(userRepository.findByName(name))
                .orElseThrow(() -> new Exception("User not found for this email :: " + email));

        if (!user.isEmpty()) {
            existUser = user.stream().allMatch(thisUser ->
                    thisUser.getEmail().equals(email) &&
                            //thisUser.getName().equals(name) &&
                            thisUser.getPassword().equals(password));
        }
        LOG.info("The user was found? : " + (existUser ? "YES" : "NO"));
        return existUser;
    }

    public User updateUser(Long userId, User fromUser) throws Exception {
        LOG.info("Looking for user : " + userId);
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId)));

        User thisUser = new User();
        if (user.isPresent()) {
            LOG.info("user is present? : " + (user.isPresent() ? "YES" : "NO"));
            thisUser = user.get();
        }

        thisUser.setName(fromUser.getName());
        thisUser.setLastName(fromUser.getLastName());
        thisUser.setEmail(fromUser.getEmail());
        final User updatedUser = userRepository.save(thisUser);
        LOG.info("User updated");
        return updatedUser;
    }

    public Map< String, Boolean > deleteUser(Long userId) throws Exception {
        LOG.info("Looking for user : " + userId);
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId)));

        userRepository.delete(user.get());
        Map < String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        LOG.info("user deleted : " + userId);
        return response;
    }

    public User resetPassword(Long userId, User fromUser) throws Exception {
        LOG.info("Looking for user : " + userId);
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId)));

        User thisUser = new User();
        if(user.isPresent()){
            LOG.info("user is present? : " + (user.isPresent() ? "YES" : "NO"));
            thisUser = user.get();
        }

        thisUser.setPassword(fromUser.getPassword());
        final User updatedUser = userRepository.save(thisUser);
        LOG.info("user updated : " + updatedUser.getId());
        return updatedUser;
    }

}
