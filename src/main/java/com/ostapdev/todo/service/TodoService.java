package com.ostapdev.todo.service;

import com.ostapdev.todo.dao.TodoDao;
import com.ostapdev.todo.model.Task;
import com.ostapdev.todo.printer.ErrorPrinter;
import com.ostapdev.todo.printer.TaskPrinter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class TodoService implements TodoDao {
    private final Map<Integer,Task> tasks = new HashMap<>();
    private Integer lastTaskId = 0;

    private final TaskPrinter taskPrinter;
    private final ErrorPrinter errorPrinter;

    @Autowired
    public TodoService(TaskPrinter taskPrinter, ErrorPrinter errorPrinter) {
        this.taskPrinter = taskPrinter;
        this.errorPrinter = errorPrinter;
    }

    @Override
    public void add(String taskDescription) {
        tasks.put(lastTaskId + 1,new Task(taskDescription,false));
        lastTaskId++;
    }

    @Override
    public void toggle(Integer taskId) {
        Task task = tasks.get(taskId);

        if (task == null){
            errorPrinter.printError("Задачи с id " + taskId + " нет");
        }

        else {
            task.setDone(!task.isDone());
            tasks.put(taskId,task);
        }
    }

    @Override
    public void print(boolean isAll) {
        if (tasks.isEmpty()){
            errorPrinter.printError("Список задач пуст");
            return;
        }

        tasks.forEach((id, task) -> {
            if (isAll) taskPrinter.printTask(task,id);

            else {
                if (!task.isDone()) taskPrinter.printTask(task,id);
            }
        });
    }

    @Override
    public void delete(Integer taskId) {
        Task task = tasks.get(taskId);
        if (task == null){
            errorPrinter.printError("Задачи с id " + taskId + " нет");
        }

        else {
            tasks.remove(taskId);
        }
    }

    @Override
    public void edit(Integer taskId, String taskDescription) {
        Task task = tasks.get(taskId);

        if (task == null){
            errorPrinter.printError("Задачи с id " + taskId + " нет");
        }

        else {
            task.setTaskDescription(taskDescription);
            tasks.put(taskId,task);
        }
    }

    @Override
    public void search(String substring) {
        tasks.entrySet().stream()
                .filter(e -> e.getValue().getTaskDescription().contains(substring))
                .forEach(e->taskPrinter.printTask(e.getValue(),e.getKey()));
    }
}
