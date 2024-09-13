package com.valencia.user.controller;

import com.valencia.user.entity.User;
import com.valencia.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getAllEUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(
            @Valid
            @RequestBody User fromUser) throws Exception {
        return ResponseEntity.ok(userService.createUser(fromUser));
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<Optional<User>> getUserById(
            @PathVariable(value = "id") Long userId) throws Exception {
        Optional<User> user = userService.getUserById(userId);
        return ResponseEntity.ok().body(user);
    }

    /**
     * Login method
    **/
    @RequestMapping(value = "/username", method = RequestMethod.GET)
    public Boolean loginByEmail(
            @RequestParam(value="email") String email,
            @RequestParam(value="password") String password) throws Exception {
        return userService.getLoginByEmail(email, password);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable(value = "id") Long userId,
            @Valid
            @RequestBody User fromUser) throws Exception {

        return ResponseEntity.ok(userService.updateUser(userId, fromUser));
    }

    @DeleteMapping("/user/{id}")
    public Map<String, Boolean> deleteUser(
            @PathVariable(value = "id") Long userId) throws Exception {
        return userService.deleteUser(userId);
    }

    @PutMapping("/reset/user/{id}")
    public ResponseEntity <User> resetPassword(
            @PathVariable(value = "id") Long userId,
            @Valid
            @RequestBody User fromUser) throws Exception {
        return ResponseEntity.ok(userService.resetPassword(userId, fromUser));
    }

}
