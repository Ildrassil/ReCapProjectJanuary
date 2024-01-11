package org.example.recapprojectjanuary.service;

import org.example.recapprojectjanuary.dto.RequestToDo;
import org.example.recapprojectjanuary.dto.ToDo;
import lombok.RequiredArgsConstructor;
import org.example.recapprojectjanuary.repo.ToDoRepo;
import org.example.recapprojectjanuary.util.IdService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebService {

    private final ToDoRepo toDoRepo;

    private final IdService idService;

    private final ChatGptService chatGptService;

    public List<ToDo> getToDoS() {

        List<ToDo> toDoList = toDoRepo.findAll();
        return toDoList;

    }

    public void putToDo(RequestToDo toDo){
        ToDo todo = new ToDo(idService.randomId(),chatGptService.spellCheck(toDo.description()),toDo.status());
        toDoRepo.save(todo);

    }

    public void updateStatusById(RequestToDo toDo, String id){
       Optional<ToDo> optionalToDo = toDoRepo.findById(id);
       if (optionalToDo.isPresent()){
           ToDo toDoToBeDelted = optionalToDo.get();
           toDoRepo.delete(toDoToBeDelted);
           toDoRepo.save(new ToDo(id, toDo.description(), toDo.status()));

       }
       else throw new RuntimeException("To Do with " + id + " does not exist");
    }

    public ToDo getToDoById(String id) {
        Optional<ToDo> optionalToDo = toDoRepo.findById(id);
        if (optionalToDo.isPresent()){
            ToDo toDo = optionalToDo.get();
            return toDo;

        }
        else throw new RuntimeException("To Do with " + id + " does not exist");
    }

    public HttpStatus deleteById(String id) {
        toDoRepo.deleteById(id);
        Optional<ToDo> emptyTodo = toDoRepo.findById(id);
        if (emptyTodo.isEmpty()){
            return HttpStatus.resolve(204);
        }
        else return HttpStatus.resolve(404);
    }
}
