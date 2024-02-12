package com.adoumadje.chatapp.controller;

import com.adoumadje.chatapp.entity.User;
import com.adoumadje.chatapp.error.EmailAlreadyTakenException;
import com.adoumadje.chatapp.error.PasswordLengthException;
import com.adoumadje.chatapp.model.LoginModel;
import com.adoumadje.chatapp.model.UserModel;
import com.adoumadje.chatapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register-user")
    public String registerUser(@Valid @RequestBody User newUser)
            throws EmailAlreadyTakenException, PasswordLengthException {
        userService.registerUser(newUser);
        return "registration successful";
    }

    @PostMapping("/login-user")
    public Map<String, String> loginUser(@RequestBody LoginModel loginModel) {
        return userService.loginUser(loginModel);
    }

    @PostMapping("/forgot-password")
    public Map<String, String> forgotPassword(@RequestBody Map<String, String> forgotter) {
        return userService.forgotPassword(forgotter);
    }

    @PutMapping("/reset-password")
    public String resetPassword(@RequestBody Map<String, String> newPassMap) {
        return userService.resetPassword(newPassMap);
    }

    @GetMapping("/logout-user")
    public String logoutUser(@RequestParam("userToken") String userToken) {
        return userService.logoutUser(userToken);
    }

    @GetMapping("/get-all-other-users")
    public List<UserModel> getAllOtherUsers(@RequestParam("userId") Long userId) {
        return userService.getAllOtherUsers(userId);
    }
}
