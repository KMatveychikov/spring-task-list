package ru.matvey.springtasklist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.matvey.springtasklist.model.Comment;
import ru.matvey.springtasklist.model.Task;
import ru.matvey.springtasklist.model.Todo;
import ru.matvey.springtasklist.model.User;
import ru.matvey.springtasklist.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final AuthService authService;


    public Task addTask(String title, String text) {
        Task task = Task.builder()
                .title(title)
                .text(text)
                .comments(new ArrayList<>())
                .todos(new ArrayList<>())
                .files(new ArrayList<>())
                .isComplete(false)
                .owner(authService.getCurrentUser())
                .responsibleUser(null)
                .performerUsers(new ArrayList<>())
                .creationTime(LocalDateTime.now())
                .build();

        taskRepository.save(task);
        authService.addTaskId(task.getOwner().get_id(), task.get_id());
        return task;
    }

    public Task getTaskById(String taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> {
            log.error("Task {} not found", taskId);
            throw new RuntimeException(String.format("Task %s not found", taskId));
        });
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByUserId(String userId) {
        User user = authService.getUserById(userId);
        return user.getTasksId().stream().map(this::getTaskById).collect(Collectors.toList());
    }


    public Task editTaskTitle(String taskId, String newTitle) {
        Task task = getTaskById(taskId);
        task.setTitle(newTitle);
        return taskRepository.save(task);
    }

    public Task editTaskText(String taskId, String newText) {
        Task task = getTaskById(taskId);
        task.setText(newText);
        return taskRepository.save(task);
    }

    public Task addComment(String taskId, String commentText) {
        Task task = getTaskById(taskId);
        Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .text(commentText)
                .author(authService.getCurrentUser())
                .postTime(LocalDateTime.now())
                .build();
        List<Comment> comments = task.getComments();
        comments.add(comment);
        task.setComments(comments);
        return taskRepository.save(task);
    }

    public Task addTodo(String taskId, String todoText) {
        Task task = getTaskById(taskId);
        Todo todo = Todo.builder()
                .id(UUID.randomUUID().toString())
                .text(todoText)
                .isDone(false)
                .build();
        List<Todo> todos = task.getTodos();
        todos.add(todo);
        task.setTodos(todos);
        return taskRepository.save(task);
    }

    public Task completeTask(String taskId) {
        Task task = getTaskById(taskId);
        task.setComplete(!task.isComplete());
        return taskRepository.save(task);
    }

    public Task setResponsibleUser(String taskId, String responsibleUserId) {
        Task task = getTaskById(taskId);
        User user = authService.getUserById(responsibleUserId);
        task.setResponsibleUser(user);
        authService.addTaskId(responsibleUserId, taskId);
        return taskRepository.save(task);
    }

    public Task addPerformerUser(String taskId, String performerUserId) {
        Task task = getTaskById(taskId);
        User user = authService.getUserById(performerUserId);

        List<User> performerUsers = task.getPerformerUsers();
        performerUsers.add(user);
        task.setPerformerUsers(performerUsers);
        authService.addTaskId(performerUserId, taskId);
        return taskRepository.save(task);
    }
    public Task removePerformerUser(String taskId, String performerUserId) {
        Task task = getTaskById(taskId);
        User user = authService.getUserById(performerUserId);

        List<User> performerUsers = task.getPerformerUsers();
        performerUsers.remove(user);
        task.setPerformerUsers(performerUsers);
        authService.removeTaskId(performerUserId, taskId);
        return taskRepository.save(task);
    }
}