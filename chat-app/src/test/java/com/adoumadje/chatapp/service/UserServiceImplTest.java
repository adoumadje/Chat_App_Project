package com.adoumadje.chatapp.service;

import com.adoumadje.chatapp.entity.PasswordResetToken;
import com.adoumadje.chatapp.entity.User;
import com.adoumadje.chatapp.error.EmailAlreadyTakenException;
import com.adoumadje.chatapp.error.PasswordLengthException;
import com.adoumadje.chatapp.model.LoginModel;
import com.adoumadje.chatapp.repository.PasswordResetTokenRepository;
import com.adoumadje.chatapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserService underTest;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(userRepository, passwordEncoder, passwordResetTokenRepository);
    }

    @Test
    void isPasswordEncodedWhenRegisterUser()
            throws PasswordLengthException, EmailAlreadyTakenException {
        // given
        User theUser = new User();
        theUser.setFullname("Bakary Sagna");
        theUser.setEmail("bakary.sagna@fmail.com");
        theUser.setPassword("123456");

        // when
        underTest.registerUser(theUser);

        // then
        verify(passwordEncoder).encode("123456");
    }

    @Test
    void isSavedUserSameAsPassedUserExceptForPasswordWhenRegisterUser()
            throws PasswordLengthException, EmailAlreadyTakenException {
        // given
        User theUser = new User();
        theUser.setFullname("Bakary Sagna");
        theUser.setEmail("bakary.sagna@fmail.com");
        theUser.setPassword("123456");

        // when
        underTest.registerUser(theUser);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        String capturedFullName = capturedUser.getFullname();
        String capturedEmail = capturedUser.getEmail();

        assertThat(capturedFullName).isEqualTo("Bakary Sagna");
        assertThat(capturedEmail).isEqualTo("bakary.sagna@fmail.com");
    }

    @Test
    void shouldThrowEmailAlreadyTakenException()
            throws PasswordLengthException, EmailAlreadyTakenException {
        // given
        User theUser = new User();
        theUser.setFullname("Bakary Sagna");
        theUser.setEmail("bakary.sagna@fmail.com");
        theUser.setPassword("123456");

        given(userRepository.findOneByEmail(theUser.getEmail()))
                .willReturn(theUser);

        // when
        // then
        assertThatThrownBy(() -> underTest.registerUser(theUser))
                .isInstanceOf(EmailAlreadyTakenException.class)
                .hasMessageContaining("This email is already taken");
    }

    @Test
    void shouldThrowPasswordLengthException()
            throws PasswordLengthException, EmailAlreadyTakenException {
        // given
        User theUser = new User();
        theUser.setFullname("Bakary Sagna");
        theUser.setEmail("bakary.sagna@fmail.com");
        theUser.setPassword("12345");

        // when
        // then
        assertThatThrownBy(() -> underTest.registerUser(theUser))
                .isInstanceOf(PasswordLengthException.class)
                .hasMessageContaining("password must be at least 6 length");
    }

    @Test
    void shouldLoginTheUser() {
        // given
        User theUser = new User();
        theUser.setFullname("Bakary Sagna");
        theUser.setEmail("bakary.sagna@fmail.com");
        theUser.setPassword("12345");

        LoginModel theModel = new LoginModel();
        theModel.setEmail("bakary.sagna@fmail.com");
        theModel.setPassword("12345");

        given(userRepository.findOneByEmail(theModel.getEmail()))
                .willReturn(theUser);

        given(passwordEncoder.matches(theModel.getPassword(), theModel.getPassword()))
                .willReturn(true);

        // when
        Map<String, String> loggedUser = underTest.loginUser(theModel);

        // then
        assertThat(loggedUser).isNotNull();
    }

    @Test
    void shouldLoginUserWithOneName() {
        // given
        User theUser = new User();
        theUser.setFullname("Bakary");
        theUser.setEmail("bakary@fmail.com");
        theUser.setPassword("12345");

        LoginModel theModel = new LoginModel();
        theModel.setEmail("bakary@fmail.com");
        theModel.setPassword("12345");

        given(userRepository.findOneByEmail(theModel.getEmail()))
                .willReturn(theUser);

        given(passwordEncoder.matches(theModel.getPassword(), theModel.getPassword()))
                .willReturn(true);

        // when
        Map<String, String> loggedUser = underTest.loginUser(theModel);

        // then
        assertThat(loggedUser).isNotNull();
    }

    @Test
    void shouldThrowInvalidEmailException() {
        // given
        LoginModel loginModel = new LoginModel();
        loginModel.setEmail("bakary.sagna@fmail.com");
        loginModel.setPassword("Password1234");
        // when
        // then
        assertThatThrownBy(() -> underTest.loginUser(loginModel))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid Email");
    }

    @Test
    void shouldThrowInvalidPasswordException() {
        // given
        User registeredUser = new User();
        registeredUser.setFullname("Bakary Sagna");
        registeredUser.setEmail("bakary.sagna@fmail.com");
        registeredUser.setPassword("Password1234");

        LoginModel loginModel = new LoginModel();
        loginModel.setEmail("bakary.sagna@fmail.com");
        loginModel.setPassword("Password1257");

        given(userRepository.findOneByEmail(loginModel.getEmail()))
                .willReturn(registeredUser);

        // when
        // then
        assertThatThrownBy(() -> underTest.loginUser(loginModel))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid Password");
    }

    @Test
    void forForgotPasswordShouldSendNewToken() {
        // given
        Map<String, String> forgotter = new HashMap<>();
        forgotter.put("email", "bakary.sagna@fmail.com");
        User theUser = new User();
        theUser.setEmail("bakary.sagna@fmail.com");
        given(userRepository.findOneByEmail(forgotter.get("email")))
                .willReturn(theUser);
        given(passwordResetTokenRepository.findOneByUser(theUser))
                .willReturn(null);

        // when
        Map<String, String> tokenMap = underTest.forgotPassword(forgotter);

        // then
        assertThat(tokenMap).isNotNull();
    }

    @Test
    void forForgotPasswordShouldSendOldToken() {
        // given
        Map<String, String> forgotter = new HashMap<>();
        forgotter.put("email", "bakary.sagna@fmail.com");
        User theUser = new User();
        theUser.setEmail("bakary.sagna@fmail.com");
        PasswordResetToken oldToken = new PasswordResetToken();
        oldToken.setUser(theUser);
        given(userRepository.findOneByEmail(forgotter.get("email")))
                .willReturn(theUser);
        given(passwordResetTokenRepository.findOneByUser(theUser))
                .willReturn(oldToken);

        // when
        Map<String, String> tokenMap = underTest.forgotPassword(forgotter);

        // then
        assertThat(tokenMap).isNotNull();
    }

    @Test
    void forForgotPasswordShouldThrowInvalidEmail() {
        // given
        Map<String, String> forgotter = new HashMap<>();
        forgotter.put("email", "bakary.sagna@fmail.com");
        given(userRepository.findOneByEmail(forgotter.get("email")))
                .willReturn(null);

        // when
        // then
        assertThatThrownBy(() -> underTest.forgotPassword(forgotter))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid Email");
    }

    @Test
    void shouldResetPasswordSuccessfully() {
        // given
        Map<String, String> newPassMap = new HashMap<>();
        String token = UUID.randomUUID().toString();
        newPassMap.put("passToken", token);
        newPassMap.put("newPass", "Password1234");
        newPassMap.put("confirmPass", "Password1234");

        User user = new User();
        user.setFullname("Bakary Sagna");

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);


        given(passwordResetTokenRepository.findOneByToken(newPassMap.get("passToken")))
                .willReturn(passwordResetToken);

        // when
        String response = underTest.resetPassword(newPassMap);

        // then
        assertThat(response).isEqualTo("Password Reset Successfully");
    }

    @Test
    void forResetPasswordShouldThrowInvalidToken() {
        // given
        Map<String, String> newPassMap = new HashMap<>();
        String token = UUID.randomUUID().toString();
        newPassMap.put("passToken", token);

        given(passwordResetTokenRepository.findOneByToken(newPassMap.get("passToken")))
                .willReturn(null);

        // when
        // then
        assertThatThrownBy(() -> underTest.resetPassword(newPassMap))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid Token");
    }

    @Test
    void forResetPasswordShouldThrowPasswordMismatch() {
        // given
        Map<String, String> newPassMap = new HashMap<>();
        String token = UUID.randomUUID().toString();
        newPassMap.put("passToken", token);
        newPassMap.put("newPass", "Password1234");
        newPassMap.put("confirmPass", "Password1256");

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);

        given(passwordResetTokenRepository.findOneByToken(newPassMap.get("passToken")))
                .willReturn(passwordResetToken);

        // when
        // then
        assertThatThrownBy(() -> underTest.resetPassword(newPassMap))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Passwords Missmacth");
    }

    @Test
    void forResetPasswordShouldThrowPasswordLengthException() {
        // given
        Map<String, String> newPassMap = new HashMap<>();
        String token = UUID.randomUUID().toString();
        newPassMap.put("passToken", token);
        newPassMap.put("newPass", "1234");
        newPassMap.put("confirmPass", "1234");

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);

        given(passwordResetTokenRepository.findOneByToken(newPassMap.get("passToken")))
                .willReturn(passwordResetToken);

        // when
        // then
        assertThatThrownBy(() -> underTest.resetPassword(newPassMap))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("password must be at least 6 length");
    }

    @Test
    void shouldLogoutUserSuccessfully() {
        // given
        String userToken = UUID.randomUUID().toString();
        User user = new User();
        user.setFullname("Bakary Sagna");

        given(userRepository.findOneByToken(userToken))
                .willReturn(user);

        // when
        String response = underTest.logoutUser(userToken);

        // then
        assertThat(response).isEqualTo("Logout Successful");
    }

    @Test
    void forLogoutUserShouldThrowForbidden() {
        // given
        String userToken = null;

        // when
        // then
        assertThatThrownBy(() -> underTest.logoutUser(userToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Forbidden: Invalid Token");
    }

    @Test
    void forLogoutUserShouldThrowInvalidToken() {
        // given
        String userToken = UUID.randomUUID().toString();

        given(userRepository.findOneByToken(userToken))
                .willReturn(null);

        // when
        // then
        assertThatThrownBy(() -> underTest.logoutUser(userToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid Token");
    }

    @Test
    void shouldGetAllOtherUsersSuccessfully() {
        // given
        Long userId = 1L;
        User user1 = new User();
        user1.setFullname("Bakary Sagna");
        User user2 = new User();
        user2.setFullname("Eduardo Camavinga");
        List<User> userList = List.of(user1, user2);

        given(userRepository.findByIdNot(userId))
                .willReturn(userList);

        // when
        Object result = underTest.getAllOtherUsers(userId);

        // then
        assertNotNull(result);
    }
}