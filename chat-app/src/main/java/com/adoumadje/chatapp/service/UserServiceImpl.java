package com.adoumadje.chatapp.service;

import com.adoumadje.chatapp.entity.PasswordResetToken;
import com.adoumadje.chatapp.entity.User;
import com.adoumadje.chatapp.error.EmailAlreadyTakenException;
import com.adoumadje.chatapp.error.PasswordLengthException;
import com.adoumadje.chatapp.model.LoginModel;
import com.adoumadje.chatapp.model.UserModel;
import com.adoumadje.chatapp.repository.PasswordResetTokenRepository;
import com.adoumadje.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
    PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public void registerUser(User newUser) throws EmailAlreadyTakenException, PasswordLengthException {
        User existingUser = userRepository.findOneByEmail(newUser.getEmail());
        if(existingUser != null) {
            throw new EmailAlreadyTakenException("This email is already taken");
        }
        if(newUser.getPassword().length() < 6) {
            throw new PasswordLengthException("password must be at least 6 length");
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
    }

    @Override
    public Map<String, String> loginUser(LoginModel loginModel) {
        Map<String, String> loggedUser = new HashMap<>();
        User user = userRepository.findOneByEmail(loginModel.getEmail());
        if(user == null) {
            throw new BadCredentialsException("Invalid Email");
        }
        if(!passwordEncoder.matches(loginModel.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }
        String userToken = UUID.randomUUID()
                + UUID.randomUUID().toString()
                + UUID.randomUUID();
        userToken = userToken.replaceAll("-", "");

        user.setToken(userToken);
        userRepository.save(user);
        loggedUser.put("id", String.valueOf(user.getId()));
        loggedUser.put("fullname", user.getFullname());
        loggedUser.put("email", user.getEmail());
        loggedUser.put("token", userToken);
        loggedUser.put("initial", getUserInitial(user.getFullname()));
        return loggedUser;
    }

    private String getUserInitial(String fullname) {
        String[] names = fullname.split(" ");
        if(names.length == 1) {
            return String.valueOf(names[0].charAt(0)).toUpperCase();
        }
        return (names[0].charAt(0)
                + String.valueOf(names[1].charAt(0))).toUpperCase();
    }

    @Override
    public Map<String, String> forgotPassword(Map<String, String> forgotter) {
        Map<String, String> tokenMap = new HashMap<>();
        User user = userRepository.findOneByEmail(forgotter.get("email"));
        if(user == null) {
            throw new BadCredentialsException("Invalid Email");
        }
        PasswordResetToken oldToken = passwordResetTokenRepository.findOneByUser(user);
        if(oldToken != null) {
            tokenMap.put("passwordResetToken", oldToken.getToken());
        } else {
            String passToken = UUID.randomUUID().toString();
            PasswordResetToken token = new PasswordResetToken();
            token.setUser(user);
            token.setToken(passToken);
            passwordResetTokenRepository.save(token);
            tokenMap.put("passwordResetToken", passToken);
        }
        return tokenMap;
    }

    @Override
    public String resetPassword(Map<String, String> newPassMap) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findOneByToken(
                newPassMap.get("passToken")
        );
        if(passwordResetToken == null) {
            throw new BadCredentialsException("Invalid Token");
        }
        if(!newPassMap.get("newPass").equals(newPassMap.get("confirmPass"))) {
            throw new RuntimeException("Passwords Missmacth");
        }
        if(newPassMap.get("newPass").length() < 6) {
            throw new BadCredentialsException("password must be at least 6 length");
        }
        User user = passwordResetToken.getUser();
        passwordResetTokenRepository.delete(passwordResetToken);
        user.setPassword(passwordEncoder.encode(newPassMap.get("newPass")));
        userRepository.save(user);
        return "Password Reset Successfully";
    }

    @Override
    public String logoutUser(String userToken) {
        if(userToken == null) {
            throw new BadCredentialsException("Forbidden: Invalid Token");
        }
        User user = userRepository.findOneByToken(userToken);
        if(user == null) {
            throw new BadCredentialsException("Invalid Token");
        }
        user.setToken(null);
        userRepository.save(user);
        return "Logout Successful";
    }

    @Override
    public List<UserModel> getAllOtherUsers(Long userId) {
        List<User> users = userRepository.findByIdNot(userId);
        List<UserModel> userModels = new ArrayList<>();
        users.forEach(user -> {
            UserModel userModel = new UserModel();
            userModel.setId(user.getId());
            userModel.setFullname(user.getFullname());
            userModel.setInitial(getUserInitial(user.getFullname()));
            userModel.setStatus(
                    user.getToken() != null? "online" : "offline"
            );
            userModel.setIsActiveChat(false);
            userModels.add(userModel);
        });
        return userModels;
    }
}
