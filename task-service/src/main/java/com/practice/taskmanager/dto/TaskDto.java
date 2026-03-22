package com.practice.taskmanager.dto;

import com.practice.taskmanager.enm.EnumTaskStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskDto {

   private Long id;
   private String taskName;
   private String taskDescription;
   private EnumTaskStatus status;
   private String assignedTo;
   private String remarks;
   private LocalDateTime creationTime;
   private LocalDateTime completionTime;
}
