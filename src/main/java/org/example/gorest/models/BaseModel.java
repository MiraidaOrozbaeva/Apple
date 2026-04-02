package org.example.gorest.models;


import org.example.gorest.JsonUtils;

public abstract class BaseModel {

    public String toJson(){
        return JsonUtils.toJson(this);
    }
}
