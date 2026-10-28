package com.taskflow.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.taskflow.model.Task;
import com.taskflow.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;


    // Constructor injection
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        task.setId(null);          // always generate a fresh id server-side
        task.setCompleted(false);  // a new task starts as not completed
        return taskRepository.save(task);
    }

    public Optional<Task> updateTask(Long id, Task task) {
        if (!taskRepository.existsById(id)) {
            return Optional.empty();
        }
        task.setId(id); // make sure we update the task named in the path
        return Optional.of(taskRepository.save(task));
    }

    public boolean deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            return false;
        }
        taskRepository.deleteById(id);
        return true;
    }

    public Optional<Task> markTaskAsComplete(Long id) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setCompleted(true);
                    return taskRepository.save(task);
                });
    }
}