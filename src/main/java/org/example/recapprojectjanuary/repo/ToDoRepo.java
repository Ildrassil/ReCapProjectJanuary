package org.example.recapprojectjanuary.repo;

import org.example.recapprojectjanuary.dto.ToDo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ToDoRepo extends MongoRepository<ToDo, String> {
}
