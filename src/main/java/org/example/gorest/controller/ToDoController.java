package org.example.gorest.controller;


import org.example.gorest.HttpRequest;
import org.example.gorest.endPoint.EndPoint;
import org.example.gorest.models.ToDo;

public class ToDoController extends HttpRequest {

    public ToDoController(String url) {
        super(url);
    }

    public ToDo[] getUserToDosById(Integer id){ //    /public/v2/users/7911361/todos
        return super.get(getEndPoint(EndPoint.PUBLIC, EndPoint.V2, EndPoint.USERS, String.valueOf(id),
                EndPoint.TODOS)).as(ToDo[].class);
    }

    public ToDo createUserToDo(ToDo toDo, Integer id){
        return super.post(getEndPoint(EndPoint.PUBLIC, EndPoint.V2, EndPoint.USERS, String.valueOf(id),
                EndPoint.TODOS), toDo.toJson()).as(ToDo.class);
    }
}
