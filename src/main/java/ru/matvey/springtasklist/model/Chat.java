package ru.matvey.springtasklist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    private String id;
    private String title;
    private List<ChatMessage> messages;
    private User owner;
    private List<User> members;
}
