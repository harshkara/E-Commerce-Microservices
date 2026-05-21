package com.taskmanager.entity;

import com.taskmanager.enm.EnumTaskStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="Task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=50,nullable = false)
    private String name;

    @Column(length=500,nullable = false)
    private String description;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime creationTime;

    @Column(length = 10)
    private String assignedTo;

    private LocalDateTime completedTime;

    @Column(length = 100)
    private String remarks;

    public void setStatus(EnumTaskStatus status){
        this.status = status.getCode();
    }

    public EnumTaskStatus getStatus(){
        return EnumTaskStatus.fromCode(status);
    }

}
