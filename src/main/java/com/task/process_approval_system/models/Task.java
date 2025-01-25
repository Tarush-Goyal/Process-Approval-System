package com.task.process_approval_system.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PENDING;

    @ManyToOne
    private User creator;

    @ManyToMany
    private List<User> approvers = new ArrayList<>();

    @ManyToMany
    private List<User> approvedUsers = new ArrayList<>();

    @ElementCollection
    private List<String> comments = new ArrayList<>();
}
