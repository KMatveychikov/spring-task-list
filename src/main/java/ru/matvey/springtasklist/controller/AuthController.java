package ru.matvey.springtasklist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @MutationMapping
    @PreAuthorize("isAnonymous()")
    public AuthResponse register(@Argument RegisterRequest request) {
        return service.register(request);
    }

    @MutationMapping
    @PreAuthorize("isAnonymous()")
    public AuthResponse login(@Argument AuthRequest request) {
        return service.authenticate(request);
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