package ru.matvey.springtasklist.model.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.matvey.springtasklist.model.User;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private User user;
}