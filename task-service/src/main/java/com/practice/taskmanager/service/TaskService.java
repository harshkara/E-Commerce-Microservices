package com.practice.taskmanager.service;


import com.practice.taskmanager.TaskManagerApplication;
import com.practice.taskmanager.dto.TaskDto;
import com.practice.taskmanager.enm.EnumTaskStatus;
import com.practice.taskmanager.entity.Task;
import com.practice.taskmanager.exception.BusinessException;
import com.practice.taskmanager.exception.NotFoundException;
import com.practice.taskmanager.mapper.TaskMapper;
import com.practice.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public long createTask(TaskDto taskDto,String user) {
        Task task = TaskMapper.createTask(taskDto,user);
        task = taskRepository.save(task);
        return task.getId();
    }

    public List<Task> getAllTask(String username) {
        return taskRepository.findAllByAssignedTo(username);
    }

    public void updateTask(TaskDto taskDto,String user) {
        if(taskDto.getStatus().equals(EnumTaskStatus.OPEN)){
            throw new BusinessException("Status cannot be open while updating the task.");
        }
        Task task = taskRepository.findById(taskDto.getId()).orElseThrow(() -> new NotFoundException("Task not found with the given id : "+taskDto.getId()));
        task.setStatus(taskDto.getStatus());
        task.setRemarks(taskDto.getRemarks());
        task.setCompletedTime(LocalDateTime.now());
        taskRepository.save(task);
        return;
    }
}
