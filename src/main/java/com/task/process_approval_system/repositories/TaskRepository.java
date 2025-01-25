package com.task.process_approval_system.repositories;

import com.task.process_approval_system.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
