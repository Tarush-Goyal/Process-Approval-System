package com.task.process_approval_system.controllers;

import com.task.process_approval_system.models.Comment;
import com.task.process_approval_system.models.Task;
import com.task.process_approval_system.models.User;
import com.task.process_approval_system.services.TaskService;
import com.task.process_approval_system.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody @Valid Task task, @RequestHeader(value = "loginID", required = true) String loginID) {
        User creator = userService.findUser(loginID);

        if (task.getApprovers() == null || task.getApprovers().size() != 3) {
            return ResponseEntity.status(400).body("Exactly three approvers are required to create a task.");
        }

        ArrayList<User>usersList = new ArrayList<>();

        for (User approver : task.getApprovers()) {
            User foundApprover = userService.findUser(approver.getId()+"");
            usersList.add(foundApprover);
        }

        task.setApprovers(usersList);

        Task createdTask = taskService.createTask(creator, task);
        return ResponseEntity.ok(createdTask);
    }

    @PostMapping("/approve/{taskID}")
    public ResponseEntity<?> approveTask(@PathVariable Long taskID, @RequestHeader(value = "loginID", required = true) String loginID) {
        User approver = userService.findUser(loginID);

        taskService.approveTask(taskID, approver);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{taskID}")
    public Task getTask(@PathVariable Long taskID, @RequestHeader(value = "loginID", required = true) String loginID) {
        User user = userService.findUser(loginID);

        return taskService.getTask(taskID);
    }

    @PostMapping("/comment/{taskID}")
    public ResponseEntity<?> addComment(@PathVariable Long taskID, @RequestHeader(value = "loginID", required = true) String loginID, @RequestBody Comment comment) {

        String commentString = comment.getComment();

        if (commentString == null || commentString.isEmpty()) {
            return ResponseEntity.status(400).body("Comment field cannot be empty");
        }

        User commentor = userService.findUser(loginID);

        taskService.addComment(taskID, commentor, commentString);

        return ResponseEntity.ok().build();
    }
}

