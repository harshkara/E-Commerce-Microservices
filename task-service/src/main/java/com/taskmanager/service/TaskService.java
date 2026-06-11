package com.taskmanager.service;


import com.common.exception.BusinessException;
import com.common.exception.NotFoundException;
import com.taskmanager.dto.TaskDto;
import com.taskmanager.enm.EnumTaskStatus;
import com.taskmanager.entity.Task;
import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.repository.TaskRepository;
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
