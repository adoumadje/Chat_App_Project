package com.adoumadje.chatapp.service;

import com.adoumadje.chatapp.entity.PrivateMessage;
import com.adoumadje.chatapp.entity.PublicMessage;
import com.adoumadje.chatapp.entity.User;
import com.adoumadje.chatapp.model.MessageModel;
import com.adoumadje.chatapp.repository.PrivateMessageRepository;
import com.adoumadje.chatapp.repository.PublicMessageRepository;
import com.adoumadje.chatapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {
    @Mock
    private PublicMessageRepository publicMessageRepository;
    @Mock
    private PrivateMessageRepository privateMessageRepository;
    @Mock
    private UserRepository userRepository;

    private MessageService underTest;

    @BeforeEach
    void setUp() {
        underTest = new MessageServiceImpl(publicMessageRepository, privateMessageRepository, userRepository);
    }

    @Test
    void shouldSavePublicMessageSuccessfully() {
        // given
        MessageModel messageModel = new MessageModel();
        messageModel.setSenderId(1L);
        messageModel.setMessage("Hello World!");

        User user = new User();
        user.setFullname("Bakary Sagna");

        given(userRepository.findById(messageModel.getSenderId()))
                .willReturn(Optional.of(user));

        // when
        underTest.savePublicMessage(messageModel);

        // then
        ArgumentCaptor<PublicMessage> publicMessageArgumentCaptor =
                ArgumentCaptor.forClass(PublicMessage.class);

        verify(publicMessageRepository)
                .save(publicMessageArgumentCaptor.capture());

        PublicMessage capturedPublicMessage = publicMessageArgumentCaptor.getValue();

        assertNotNull(capturedPublicMessage);

    }

    @Test
    void forSavePublicMessageShouldThrowForbidden() {
        // given
        MessageModel messageModel = new MessageModel();
        messageModel.setSenderId(1L);

        given(userRepository.findById(messageModel.getSenderId()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.savePublicMessage(messageModel))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Forbidden");

    }

    @Test
    void shouldGetAllChatRoomMessagesSuccessfully() {
        // given
        User user1 = new User();
        user1.setFullname("Bakary Sagna");
        PublicMessage message1 = new PublicMessage();
        message1.setMessage("Hello World!");
        message1.setSender(user1);
        User user2 = new User();
        user2.setFullname("Eduardo");
        PublicMessage message2 = new PublicMessage();
        message2.setMessage("Fuck Message");
        message2.setSender(user2);

        given(publicMessageRepository.findAll())
                .willReturn(List.of(message1, message2));

        // when
        Object result = underTest.getAllChatRoomMessages();

        // then
        assertNotNull(result);

    }

    @Test
    void shouldSavePrivateMessageSuccessfully() {
        // given
        MessageModel messageModel = new MessageModel();
        messageModel.setSenderId(1L);
        messageModel.setReceiverId(2L);
        messageModel.setMessage("Hello World!");

        User user1 = new User();
        user1.setFullname("Bakary Sagna");

        User user2 = new User();
        user2.setFullname("Eduardo Camavinga");

        given(userRepository.findById(messageModel.getSenderId()))
                .willReturn(Optional.of(user1));

        given(userRepository.findById(messageModel.getReceiverId()))
                .willReturn(Optional.of(user2));

        // when
        underTest.savePrivateMessage(messageModel);

        // then
        ArgumentCaptor<PrivateMessage> privateMessageArgumentCaptor =
                ArgumentCaptor.forClass(PrivateMessage.class);

        verify(privateMessageRepository)
                .save(privateMessageArgumentCaptor.capture());

        PrivateMessage capturedPrivateMessage = privateMessageArgumentCaptor.getValue();

        assertNotNull(capturedPrivateMessage);
    }

    @Test
    void forSavePrivateMessageShouldThrowForbidden() {
        // given
        MessageModel messageModel = new MessageModel();
        messageModel.setSenderId(1L);
        messageModel.setReceiverId(2L);
        messageModel.setMessage("Hello World!");

        User user1 = new User();
        user1.setFullname("Bakary Sagna");

        given(userRepository.findById(messageModel.getSenderId()))
                .willReturn(Optional.of(user1));

        given(userRepository.findById(messageModel.getReceiverId()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.savePrivateMessage(messageModel))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Forbidden");
    }

    @Test
    void shouldGetUserPrivateMessagesSuccessfully() {
        // given
        Long userId = 1L;
        User user1 = new User();
        user1.setFullname("Bakary Sagna");
        User user2 = new User();
        user2.setFullname("Eduardo Camavinga");

        PrivateMessage privateMessage1 = new PrivateMessage();
        privateMessage1.setMessage("Hello Cama");
        privateMessage1.setSender(user1);
        privateMessage1.setReceiver(user2);
        PrivateMessage privateMessage2 = new PrivateMessage();
        privateMessage2.setMessage("Hello Baka");
        privateMessage2.setSender(user2);
        privateMessage2.setReceiver(user1);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user1));

        given(privateMessageRepository.findBySenderOrReceiver(user1, user1))
                .willReturn(List.of(privateMessage1, privateMessage2));

        // when
        Object result = underTest.getUserPrivateMessages(userId);

        // then
        assertNotNull(result);
    }

    @Test
    void forGetUserPrivateMessagesShouldThrowForbidden() {
        // given
        Long userId = 1L;

        given(userRepository.findById(userId))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getUserPrivateMessages(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Forbidden");
    }
}