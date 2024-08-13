package com.valencia.user.controller;

import com.valencia.user.entity.User;
import com.valencia.user.exception.ResourceNotFoundException;
import com.valencia.user.repository.UserRepository;
import com.valencia.user.service.SequenceGeneratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping("/users")
    public List<User> getAllEUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User fromUser) throws ResourceNotFoundException {

        User user = new User();
        user.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
        user.setName(fromUser.getName());
        user.setLastName(fromUser.getLastName());
        user.setEmail(fromUser.getEmail());
        user.setPassword(fromUser.getPassword());

        return ResponseEntity.ok(userRepository.save(user));
    }

    //@GetMapping("/user/{id}")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<Optional<User>> getUserById(@PathVariable(value = "id") Long userId) throws Exception {
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found for this id :: " + userId)));
        return ResponseEntity.ok().body(user);
    }

    /**
     * Login method
    **/
    @RequestMapping(value = "/username", method = RequestMethod.GET)
    public Boolean getUserByEmail(@RequestParam(value="email") String email, @RequestParam(value="password") String password) throws Exception {
        Boolean existUser = false;
        List<User> user = Optional.ofNullable(userRepository.findByEmailIs(email))
        //List<User> user = Optional.ofNullable(userRepository.findByName(name))
                .orElseThrow(() -> new Exception("User not found for this email :: " + email));

        if (!user.isEmpty()) {
            existUser = user.stream().allMatch(thisUser ->
                    thisUser.getEmail().equals(email) &&
                    //thisUser.getName().equals(name) &&
                    thisUser.getPassword().equals(password));
        }
        return existUser;
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody User fromUser) throws ResourceNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId)));

        User thisUser = new User();
        if (user.isPresent()) {
            thisUser = user.get();
        }

        thisUser.setName(fromUser.getName());
        thisUser.setLastName(fromUser.getLastName());
        thisUser.setEmail(fromUser.getEmail());
        final User updatedUser = userRepository.save(thisUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/user/{id}")
    public Map< String, Boolean > deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId)));

        userRepository.delete(user.get());
        Map < String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @PutMapping("/reset/user/{id}")
    public ResponseEntity <User> resetPassword(@PathVariable(value = "id") Long userId, @Valid @RequestBody User fromUser) throws ResourceNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId)));

        User thisUser = new User();
        if(user.isPresent()){
            thisUser = user.get();
        }

        thisUser.setPassword(fromUser.getPassword());
        final User updatedUser = userRepository.save(thisUser);
        return ResponseEntity.ok(updatedUser);
    }

}
