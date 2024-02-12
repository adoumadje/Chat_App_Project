package com.adoumadje.chatapp.service;

import com.adoumadje.chatapp.entity.User;
import com.adoumadje.chatapp.error.EmailAlreadyTakenException;
import com.adoumadje.chatapp.error.PasswordLengthException;
import com.adoumadje.chatapp.model.LoginModel;
import com.adoumadje.chatapp.model.UserModel;

import java.util.List;
import java.util.Map;

public interface UserService {
    void registerUser(User newUser) throws EmailAlreadyTakenException, PasswordLengthException;

    Map<String, String> loginUser(LoginModel loginModel);

    Map<String, String> forgotPassword(Map<String, String> forgotter);

    String resetPassword(Map<String, String> newPassMap);

    String logoutUser(String userToken);

    List<UserModel> getAllOtherUsers(Long userId);
}
