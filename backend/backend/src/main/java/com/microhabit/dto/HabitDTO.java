package com.microhabit.dto;

public class HabitDTO {
    private Long id;
    private String title;
    private String description;
    private boolean completedToday;

    public HabitDTO() {
    }

    public HabitDTO(Long id, String title, String description, boolean completedToday) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completedToday = completedToday;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompletedToday() {
        return completedToday;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompletedToday(boolean completedToday) {
        this.completedToday = completedToday;
    }
}
