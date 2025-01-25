package com.task.process_approval_system.services;

import com.task.process_approval_system.models.Task;
import com.task.process_approval_system.models.TaskStatus;
import com.task.process_approval_system.models.User;
import com.task.process_approval_system.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final EmailService emailService;

    public TaskService(TaskRepository taskRepository, EmailService emailService) {
        this.taskRepository = taskRepository;
        this.emailService = emailService;
    }


    public Task createTask(User user, Task task) {
        task.setCreator(user);
        for(User approver: task.getApprovers()){
            emailService.sendEmail(approver.getEmail(), "New Task Created", "You have been set as a approver on a new task.");
        }
        return taskRepository.save(task);
    }

    public void approveTask(Long taskId, User approver) {
        Task task = getTask(taskId);

        if (!task.getApprovers().contains(approver)) {
            throw new RuntimeException("You are not an approver for this task.");
        }

        if (task.getApprovedUsers().contains(approver)) {
            throw new RuntimeException("You have already approved this task.");
        }

        task.getApprovedUsers().add(approver);

        if (task.getApprovedUsers().size() == task.getApprovers().size()) {
            task.setStatus(TaskStatus.APPROVED);
            emailService.sendEmail(task.getCreator().getEmail(), "Task Approved", "Your task has been approved.");

            for (User user : task.getApprovers()) {
                emailService.sendEmail(user.getEmail(), "Task Approved", "The task has been approved.");
            }
        } else {
            emailService.sendEmail(task.getCreator().getEmail(), "1 New Approval Received", "Your task has received a new approval");
        }

        taskRepository.save(task);
    }

    public void addComment(Long taskId, User user, String comment) {
        Task task = getTask(taskId);
        System.out.println("comment: "+comment);
        System.out.println(task.getApprovers());
        System.out.println(user.getId());
        if (!task.getApprovers().contains(user)) {
            throw new RuntimeException("You are not authorized to comment on this task.");
        }

        task.getComments().add(comment);
        taskRepository.save(task);
    }

    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
    }



}
