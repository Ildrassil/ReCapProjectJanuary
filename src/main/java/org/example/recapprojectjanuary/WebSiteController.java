package org.example.recapprojectjanuary;

import org.example.recapprojectjanuary.DTO.ErrorMessage;
import org.example.recapprojectjanuary.DTO.RequestToDo;
import org.example.recapprojectjanuary.DTO.ToDo;
import lombok.RequiredArgsConstructor;
import org.example.recapprojectjanuary.UTIL.WebService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WebSiteController {

    public final WebService webService;



    @GetMapping("/todo")
    public List<ToDo> getToDos(){
        return webService.getToDoS();
    }

    @GetMapping("/todo/{id}")
    public ToDo getToDoById(@PathVariable String id){
        return webService.getToDoById(id);

    }

    @PostMapping("/todo")
    public void postToDos(@RequestBody RequestToDo todo ){
        webService.putToDo(todo);

    }

    @PutMapping("/todo/{id}")
    public void putToDo(@RequestBody RequestToDo toDo,
                        @PathVariable String id){
        webService.updateStatusById(toDo, id);

    }

    @DeleteMapping("/todo/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteToDo(@PathVariable String id) throws RuntimeException{
        HttpStatus httpStatus = webService.deleteById(id);
        if (httpStatus == HttpStatus.NO_CONTENT){

        }
        else {
            throw new RuntimeException("No Element found with this " + id);
        }
    }
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage exceptionHandler(RuntimeException runex){
        return new ErrorMessage("No Element found " + runex.getMessage());
    }

}
