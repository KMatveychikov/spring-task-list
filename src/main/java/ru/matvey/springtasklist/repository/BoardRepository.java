package ru.matvey.springtasklist.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.matvey.springtasklist.model.Board;
import ru.matvey.springtasklist.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {
    List<Board> findBoardsByOwner(User user);
}
