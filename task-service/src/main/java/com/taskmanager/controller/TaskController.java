package com.taskmanager.controller;

import com.common.security.UserPrincipal;
import com.taskmanager.dto.ResponseDto;
import com.taskmanager.dto.TaskDto;
import com.taskmanager.entity.Task;
import com.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> create(@RequestBody TaskDto taskDto) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = taskService.createTask(taskDto,userPrincipal.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("Task created successfully with id : "+id));
    }

    @PostMapping("/getAllTask")
    public ResponseEntity<ResponseDto> getAllTask() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Task> list  = taskService.getAllTask(userPrincipal.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(list));
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseDto> update(@RequestBody TaskDto taskDto) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        taskService.updateTask(taskDto,userPrincipal.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Task updated successfully."));
    }

}
