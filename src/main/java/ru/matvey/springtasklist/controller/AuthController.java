package ru.matvey.springtasklist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.matvey.springtasklist.model.User;
import ru.matvey.springtasklist.model.dto.auth.AuthRequest;
import ru.matvey.springtasklist.model.dto.auth.AuthResponse;
import ru.matvey.springtasklist.model.dto.auth.RegisterRequest;
import ru.matvey.springtasklist.service.AuthService;

import java.util.List;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest request
    )  {
        return ResponseEntity.ok()
                .body(service.authenticate(request));
    }

    @GetMapping("/get_all")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/get_name_by_id/{id}")
    public ResponseEntity<String> getUsernameById(@PathVariable String id) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(service.getUsernameById(id));
    }

}