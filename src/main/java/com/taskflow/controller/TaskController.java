package com.taskflow.controller;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskflow.model.Task;
import com.taskflow.service.TaskService;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ChatClient chatClient;

    // Constructor injection of the service, and the ChatClient from the auto-configured builder
    public TaskController(TaskService taskService, ChatClient.Builder chatClientBuilder) {
        this.taskService = taskService;
        this.chatClient = chatClientBuilder.build();
    }


    // GET /api/tasks/summary - Task 6: Spring AI summary (exact path wins over {id})
    @GetMapping("/summary")
    public ResponseEntity<String> getTaskSummary() {
        List<Task> tasks = taskService.findAllTasks();

        String summary = chatClient.prompt()
                .user(u -> u.text("""
                        Here is my to-do list:
                        {tasks}

                        In two or three short plain-English sentences, tell me what is done
                        and what is still pending. Be brief and friendly.
                        """).param("tasks", tasks.toString()))
                .call()
                .content();

        return ResponseEntity.ok(summary);
    }
    
    // GET /api/tasks - get all tasks
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAllTasks());
    }

      // GET /api/tasks/{id} - get one task by id
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.findTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/tasks - create a new task (201 Created)
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task created = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/tasks/{id} - update an existing task
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/tasks/{id} - delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!taskService.deleteTask(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    // PUT /api/tasks/{id}/complete - mark a task as complete
    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> markTaskAsComplete(@PathVariable Long id) {
        return taskService.markTaskAsComplete(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
