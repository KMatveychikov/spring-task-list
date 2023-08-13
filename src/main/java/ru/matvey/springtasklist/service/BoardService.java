package ru.matvey.springtasklist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.matvey.springtasklist.model.Board;
import ru.matvey.springtasklist.model.Task;
import ru.matvey.springtasklist.model.User;
import ru.matvey.springtasklist.repository.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final TaskService taskService;
    private final AuthService authService;

    public Board addTaskToBoard(String boardId, String taskTitle, String taskText) {
        Board board = getBoardById(boardId);
        Task task = taskService.addTask(taskTitle, taskText);
        ArrayList<String> tasksId = board.getTasks();
        tasksId.add(task.get_id());
        board.setTasks(tasksId);
        boardRepository.save(board);
        return board;
    }

    public Board setTaskIndex(String boardId, String taskId, Integer index) {
        Board board = getBoardById(boardId);
        if (board.getTasks().contains(taskId)) {
            ArrayList<String> tasks = board.getTasks();
            tasks.remove(taskId);
            tasks.add(index, taskId);
            board.setTasks(tasks);
        } else {
            log.error("Task {} not found on board {}", taskId, boardId);
            throw new RuntimeException(String.format("Task %s not found ob board %s", taskId, boardId));
        }
        return board;
    }

    public Board getBoardById(String boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> {
            log.error("Board {} not found", boardId);
            throw new RuntimeException(String.format("Board %s not found", boardId));
        });
    }

    public Board getDefaultBoardByUserId(String userId) {
        User user = authService.getUserById(userId);
        return boardRepository.findBoardsByOwner(user).stream().filter(board -> Objects.equals(board.getTitle(), "Unsorted")).findFirst().orElseThrow(() -> {
            log.error("Default board not found");
            throw new RuntimeException("Default board not found");
        });
    }


    public void deleteBoard(String boardId) {
        Board board = getBoardById(boardId);
        if (!Objects.equals(board.getTitle(), "Unsorted")) {
            Stream<String> t = board.getTasks().stream().peek(taskId -> {
                Task task = taskService.getTaskById(taskId);
                addTaskToBoard(getDefaultBoardByUserId(authService.getCurrentUserId()).get_id(), task.getTitle(), task.getText());
            });
            boardRepository.delete(board);
        } else {
            log.error("You can't delete default board");
            throw new RuntimeException("You can't delete default board");
        }
    }

}
