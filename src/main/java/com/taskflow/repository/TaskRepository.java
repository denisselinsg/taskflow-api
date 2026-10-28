package com.taskflow.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.taskflow.model.Task;

@Repository
public class TaskRepository {

    // HashMap: look a task up directly by its id (the key).
    private final Map<Long, Task> tasks = new HashMap<>();
    private Long idCounter = 1L; // simple counter to generate our own ids

    // Seed a few sample tasks at startup so the API is not empty
    public TaskRepository() {
        save(new Task(null, "Task 1", "Do the Assignment",true));
        save(new Task(null, "Task 2", "Complete TaskFlow API ", false));
        save(new Task(null, "Task 3", "Push code to GitHub", false));

    }

    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idCounter++);
        }
        tasks.put(task.getId(), task);
        return task;
    }

    public void deleteById(Long id) {
        tasks.remove(id);
    }

    public boolean existsById(Long id) {
        return tasks.containsKey(id);
    }
}
