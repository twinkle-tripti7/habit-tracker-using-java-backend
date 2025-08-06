package com.microhabit.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "habits")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "habit_completion", joinColumns = @JoinColumn(name = "habit_id"))
    @MapKeyColumn(name = "date")
    @Column(name = "completed")
    private Map<String, Boolean> completionMap = new HashMap<>();

    public Habit() {
    }

    public Habit(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Map<String, Boolean> getCompletionMap() {
        return completionMap;
    }

    public void setCompletionMap(Map<String, Boolean> completionMap) {
        this.completionMap = completionMap;
    }

}
