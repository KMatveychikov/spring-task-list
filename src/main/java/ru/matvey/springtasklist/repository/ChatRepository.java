package ru.matvey.springtasklist.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.matvey.springtasklist.model.Chat;

public interface ChatRepository extends MongoRepository<Chat, String> {
}
