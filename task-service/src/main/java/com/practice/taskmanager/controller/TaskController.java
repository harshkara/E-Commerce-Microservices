package com.practice.taskmanager.controller;

import com.practice.taskmanager.dto.ResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> create() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Task created successfully."));
    }

}
