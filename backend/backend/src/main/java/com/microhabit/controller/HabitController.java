package com.microhabit.controller;

import com.microhabit.model.Habit;
import com.microhabit.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*") // Enable for frontend
@RestController
@RequestMapping("/api/habits")
public class HabitController {

    @Autowired
    private HabitService habitService;

    @GetMapping
    public List<Habit> getAllHabits() {
        return habitService.getAllHabits();
    }

    @PostMapping
    public Habit addHabit(@RequestBody Habit habit) {
        return habitService.addHabit(habit);
    }

    @GetMapping("/{id}")
    public Habit getHabitById(@PathVariable Long id) {
        return habitService.getHabitById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Habit updateHabit(@PathVariable Long id, @RequestBody Habit habit) {
        return habitService.updateHabit(id, habit);
    }

    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
    }

    @PatchMapping("/{id}/toggle")
    public Habit toggleHabitCompletion(@PathVariable Long id) {
        return habitService.toggleHabitCompletion(id);
    }

    @GetMapping("/daily-summary")
    public Map<LocalDate, Long> getDailyHabitSummary() {
        return habitService.getDailyHabitSummary();
    }

}
