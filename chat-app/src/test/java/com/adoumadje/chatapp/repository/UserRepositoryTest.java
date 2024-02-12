package com.adoumadje.chatapp.repository;

import com.adoumadje.chatapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository underTest;

    @Test
    void shouldFindOneUserByEmail() {
        // given
        User theUser = new User();
        theUser.setFullname("Bakary Sagna");
        theUser.setEmail("bakary.sagna@fmail.com");
        theUser.setPassword("123456");
        User savedUser = underTest.save(theUser);

        // when
        User foundUser = underTest.findOneByEmail("bakary.sagna@fmail.com");

        // then
        assertThat(foundUser).isEqualTo(savedUser);
    }

    @Test
    void shouldFindOneByToken() {
        // given
        User theUser = new User();
        theUser.setFullname("Bakary Sagna");
        theUser.setEmail("bakary.sagna@fmail.com");
        theUser.setPassword("Password1234");
        String token = UUID.randomUUID() + UUID.randomUUID().toString() + UUID.randomUUID();
        token = token.replaceAll("-", "");
        theUser.setToken(token);

        User savedUser = underTest.save(theUser);

        // when
        User foundUser = underTest.findOneByToken(token);

        // then
        assertThat(foundUser).isEqualTo(savedUser);
    }

    @Test
    void shouldFindByIdNot() {
        // given
        User user1 = new User();
        user1.setFullname("Bakary Sagna");
        user1.setEmail("bakary.sagna@fmail.com");
        user1.setPassword("Password1234");

        User user2 = new User();
        user2.setFullname("Eduardo Camavinga");
        user2.setEmail("eduardo.camavinga@fmail.com");
        user2.setPassword("Password1234");

        User user3 = new User();
        user3.setFullname("Fede Valverde");
        user3.setEmail("fede.valverde@fmail.com");
        user3.setPassword("Password1234");

        User savedUser1 = underTest.save(user1);
        User savedUser2 = underTest.save(user2);
        User savedUser3 = underTest.save(user3);

        Long theId = savedUser1.getId();
        List<User> theList = List.of(savedUser2, savedUser3);

        // when
        List<User> foundedList = underTest.findByIdNot(theId);

        // then
        assertThat(foundedList).isEqualTo(theList);
    }
}