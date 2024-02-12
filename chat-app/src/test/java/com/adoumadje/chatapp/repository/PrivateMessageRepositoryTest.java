package com.adoumadje.chatapp.repository;

import com.adoumadje.chatapp.entity.PrivateMessage;
import com.adoumadje.chatapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PrivateMessageRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrivateMessageRepository underTest;

    @Test
    void shouldFindBySenderOrReceiver() {
        // given
        User user1 = new User();
        user1.setFullname("Bakary Sagna");
        user1.setEmail("bakary.sagna@fmail.com");
        user1.setPassword("Password1234");
        user1 = userRepository.save(user1);

        User user2 = new User();
        user2.setFullname("Eduardo Camavinga");
        user2.setEmail("eduardo.camavinga@fmail.com");
        user2.setPassword("Password1234");
        user2 = userRepository.save(user2);

        String message1 = "Hello Camavinga";
        String message2 = "Hello Sagna";

        PrivateMessage privateMessage1 = new PrivateMessage();
        privateMessage1.setSender(user1);
        privateMessage1.setReceiver(user2);
        privateMessage1.setMessage(message1);
        privateMessage1 = underTest.save(privateMessage1);

        PrivateMessage privateMessage2 = new PrivateMessage();
        privateMessage2.setSender(user2);
        privateMessage2.setReceiver(user1);
        privateMessage2.setMessage(message2);
        privateMessage2 = underTest.save(privateMessage2);

        List<PrivateMessage> theList = List.of(privateMessage1, privateMessage2);

        // when
        List<PrivateMessage> foundPrivateMessage = underTest.findBySenderOrReceiver(user1, user1);

        // then
        assertThat(foundPrivateMessage).isEqualTo(theList);
    }
}