package com.adoumadje.chatapp.repository;

import com.adoumadje.chatapp.entity.PasswordResetToken;
import com.adoumadje.chatapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PasswordResetTokenRepositoryTest {
    @Autowired
    private PasswordResetTokenRepository underTest;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindOneByUser() {
        // given
        User theUser = new User();
        theUser.setFullname("Bakary Sagna");
        theUser.setEmail("bakary.sagna@fmail.com");
        theUser.setPassword("Password1234");
        theUser = userRepository.save(theUser);

        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(theUser);
        PasswordResetToken savedResetToken = underTest.save(passwordResetToken);

        // when
        PasswordResetToken foundResetToken = underTest.findOneByUser(theUser);

        // then
        assertThat(foundResetToken).isEqualTo(savedResetToken);
    }

    @Test
    void shouldFindOneByToken() {
        // given
        User theUser = new User();
        theUser.setFullname("Bakary Sagna");
        theUser.setEmail("bakary.sagna@fmail.com");
        theUser.setPassword("Password1234");
        theUser = userRepository.save(theUser);

        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(theUser);
        PasswordResetToken savedResetToken = underTest.save(passwordResetToken);

        // when
        PasswordResetToken foundResetToken = underTest.findOneByToken(token);

        // then
        assertThat(foundResetToken).isEqualTo(savedResetToken);
    }
}