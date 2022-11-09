package com.newsnow.imagewrapper.domain;

import java.util.List;

public class TaskList {
    private List<Task> tasks;

    public TaskList(){}
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
