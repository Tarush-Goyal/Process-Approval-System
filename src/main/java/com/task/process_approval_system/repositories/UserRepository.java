package com.task.process_approval_system.repositories;

import com.task.process_approval_system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
