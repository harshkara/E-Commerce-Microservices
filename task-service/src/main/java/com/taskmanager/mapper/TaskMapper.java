package com.taskmanager.mapper;

import com.taskmanager.dto.TaskDto;
import com.taskmanager.enm.EnumTaskStatus;
import com.taskmanager.entity.Task;

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
