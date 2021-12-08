package com.ostapdev.todo.service;

import com.ostapdev.todo.dto.TaskDto;

import java.util.List;

public interface TaskService {
    void add(String taskDescription);

    void toggle(Long taskId);

    List<TaskDto> getTasks(Boolean isAll, String target);

    void delete(Long taskId);

    void edit(Long taskId,String taskDescription);
}
