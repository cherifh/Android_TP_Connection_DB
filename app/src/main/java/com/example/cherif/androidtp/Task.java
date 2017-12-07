package com.example.cherif.androidtp;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by A654911 on 06/12/2017.
 */

public class Task implements Serializable{

    private int id, status;
    private String task, description, created_at;

    @Override
    public String toString() {
        return task + "\t\t\t" +  description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


}
