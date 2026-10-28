package com.taskflow.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor      // Generates a no-args constructor
@AllArgsConstructor     // Generates an all-args constructor
public class Task {
    private Long id;        // Unique identifier
    private String title;   // Task title 
    private String description; // Task description
    private boolean completed; // Flag to indicate completion

}
