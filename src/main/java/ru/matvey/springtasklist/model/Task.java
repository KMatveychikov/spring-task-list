package ru.matvey.springtasklist.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private String _id;
    private String title;
    private String text;
    private List<Comment> comments;
    private List<Todo> todos;
    private List<FileInfo> files;
    private boolean isComplete;

    private User owner;
    private User responsibleUser;
    private List<User> performerUsers;

    private LocalDateTime creationTime;
    private LocalDateTime endTime;
}
