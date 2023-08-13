package ru.matvey.springtasklist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.matvey.springtasklist.model.Chat;
import ru.matvey.springtasklist.model.ChatMessage;
import ru.matvey.springtasklist.model.User;
import ru.matvey.springtasklist.repository.ChatRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final AuthService authService;

    public Chat addChat(String title) {
        Chat chat = Chat.builder()
                .owner(authService.getCurrentUser())
                .title(title)
                .messages(new ArrayList<>())
                .members(new ArrayList<>())
                .build();
        return chatRepository.save(chat);
    }

    public List<Chat> getChatsByCurrentUser(){
        return authService.getCurrentUser().getChatsId().stream().map(this::getChatById).collect(Collectors.toList());
    }

    public Chat getChatById(String chatId) {
        return chatRepository.findById(chatId).orElseThrow(() -> {
            log.error("chat {} not found", chatId);
            throw new RuntimeException(String.format("chat %s not found", chatId));
        });
    }

    public List<ChatMessage> getChatMessages(String chatId) {
        Chat chat = getChatById(chatId);
        return chat.getMessages();
    }

    public List<User> getChatMembers(String chatId){
        Chat chat = getChatById(chatId);
        return chat.getMembers();
    }

    public Chat addMessage(String chatId, String text) {
        ChatMessage chatMessage = ChatMessage.builder()
                .id(UUID.randomUUID().toString())
                .author(authService.getCurrentUserName())
                .timeStamp(TimeService.getTimeStamp(LocalDateTime.now()))
                .text(text)
                .build();

        Chat chat = getChatById(chatId);
        List<ChatMessage> messages = chat.getMessages();
        messages.add(chatMessage);
        chat.setMessages(messages);
        return chatRepository.save(chat);
    }

    public Chat addUserToChat(String chatId, String userId){
        Chat chat = getChatById(chatId);
        User user = authService.getUserById(userId);

        List<User> members = chat.getMembers();
        members.add(user);
        chat.setMembers(members);
        chatRepository.save(chat);
        authService.addChatId(userId,chatId);
        return chat;
    }

    public Chat removeUserFromChat(String chatId, String userId){
        Chat chat = getChatById(chatId);
        User user = authService.getUserById(userId);

        List<User> members = chat.getMembers();
        members.remove(user);
        chat.setMembers(members);
        chatRepository.save(chat);
        authService.removeChatId(userId,chatId);
        return chat;
    }


}
