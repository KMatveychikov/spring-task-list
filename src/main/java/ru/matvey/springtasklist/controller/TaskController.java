package ru.matvey.springtasklist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import ru.matvey.springtasklist.model.Task;
import ru.matvey.springtasklist.service.TaskService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @MutationMapping
    public Task addTask(@Argument String title, @Argument String text) {
        return taskService.addTask(title, text);
    }

    @QueryMapping
    public List<Task> getTasksByUserId(@Argument String userId) {
        return taskService.getTasksByUserId(userId);
    }

    @MutationMapping
    public Task editTaskTitle(@Argument String taskId, @Argument String newTitle) {
        return taskService.editTaskTitle(taskId, newTitle);
    }

    @MutationMapping
    public Task editTaskText(@Argument String taskId, @Argument String newText) {
        return taskService.editTaskText(taskId, newText);
    }

    @MutationMapping
    public Task addComment(@Argument String taskId, @Argument String commentText) {
        return taskService.addComment(taskId, commentText);
    }

    @MutationMapping
    public Task addTodo(@Argument String taskId, @Argument String todoText) {
        return taskService.addTodo(taskId, todoText);
    }

    @MutationMapping
    public Task completeTask(@Argument String taskId) {
        return taskService.completeTask(taskId);
    }

    @MutationMapping
    public Task setResponsibleUser(@Argument String taskId, @Argument String responsibleUserId) {
        return taskService.setResponsibleUser(taskId, responsibleUserId);
    }

    @MutationMapping
    public Task addPerformerUser(@Argument String taskId, @Argument String performerUserId) {
        return taskService.addPerformerUser(taskId, performerUserId);
    }

    @MutationMapping
    public Task removePerformerUser(String taskId, String performerUserId) {
        return taskService.removePerformerUser(taskId, performerUserId);
    }
}
