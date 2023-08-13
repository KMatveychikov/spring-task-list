package ru.matvey.springtasklist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.matvey.springtasklist.model.Board;
import ru.matvey.springtasklist.model.User;
import ru.matvey.springtasklist.model.dto.auth.AuthRequest;
import ru.matvey.springtasklist.model.dto.auth.AuthResponse;
import ru.matvey.springtasklist.model.dto.auth.RegisterRequest;
import ru.matvey.springtasklist.repository.BoardRepository;
import ru.matvey.springtasklist.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BoardRepository boardRepository;


    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .chatsId(new ArrayList<>())
                .tasksId(new ArrayList<>())
                .build();
        userRepository.save(user);

        Board board = Board.builder()
                .owner(user)
                .title("Unsorted")
                .tasks(new ArrayList<>())
                .build();
        boardRepository.save(board);

        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("user {} not found", userId);
            throw new RuntimeException("user not found");
        });
    }


    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public User getCurrentUser() {
        return userRepository.findByEmail(getCurrentUserEmail()).orElseThrow(() -> {
            log.error("user not found");
            throw new RuntimeException("user not found");
        });
    }

    public String getCurrentUserId() {
        return getCurrentUser().get_id();
    }

    public String getCurrentUserName() {
        return getCurrentUser().getUsername();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String getUsernameById(String id) throws RuntimeException {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error("User {} not found", id);
            return new RuntimeException("user not found");
        });
        return user.getName();
    }

    public boolean isPresentUser(String userId) {
        if (userRepository.existsById(userId)) {
            return true;
        } else {
            return false;
        }
    }

    public User addChatId(String userId, String chatId) {
        User user = getUserById(userId);
        List<String> chats = user.getChatsId();
        if (!chats.contains(chatId)) {
            chats.add(chatId);
            user.setChatsId(chats);
            return userRepository.save(user);
        } else {
            return user;
        }
    }

    public User addTaskId(String userId, String taskId) {
        User user = getUserById(userId);
        List<String> tasks = user.getTasksId();
        if (!tasks.contains(taskId)) {
            tasks.add(taskId);
            user.setTasksId(tasks);
            return userRepository.save(user);
        } else {
            return user;
        }
    }

    public User removeChatId(String userId, String chatId) {
        User user = getUserById(userId);
        List<String> chats = user.getChatsId();
        chats.remove(chatId);
        user.setChatsId(chats);
        return userRepository.save(user);
    }

    public User removeTaskId(String userId, String taskId) {
        User user = getUserById(userId);
        List<String> tasks = user.getTasksId();
        tasks.remove(taskId);
        user.setTasksId(tasks);
        return userRepository.save(user);
    }
}