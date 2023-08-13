package ru.matvey.springtasklist.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.matvey.springtasklist.model.Task;
@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
}
