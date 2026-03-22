package com.practice.taskmanager.mapper;

import com.practice.taskmanager.dto.TaskDto;
import com.practice.taskmanager.enm.EnumTaskStatus;
import com.practice.taskmanager.entity.Task;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class TaskMapper {

    public static Task createTask(TaskDto taskDto,String user) {

        Task task = new Task();
        task.setName(taskDto.getTaskName());
        task.setDescription(taskDto.getTaskDescription());
        task.setCreationTime(LocalDateTime.now());
        task.setAssignedTo(user);
        task.setStatus(EnumTaskStatus.OPEN);
        return task;
    }
}
