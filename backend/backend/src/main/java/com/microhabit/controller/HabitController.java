package com.microhabit.controller;

import com.microhabit.dto.HabitDTO;
import com.microhabit.model.Habit;
import com.microhabit.repository.HabitRepository;
import com.microhabit.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/habits")
public class HabitController {

    @Autowired
    private HabitService habitService;

    @GetMapping
    public List<HabitDTO> getHabits() {
        return habitService.getAllHabits();
    }

    @PostMapping
    public ResponseEntity<Habit> createHabit(@RequestBody HabitDTO habitDto) {
        Habit savedHabit = habitService.createHabit(habitDto);
        return ResponseEntity.ok(savedHabit);
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

    @PutMapping("/{id}/toggle")
    public Habit toggleCompletion(@PathVariable Long id) {
        return habitService.toggleHabitCompletion(id);
    }

    @GetMapping("/daily-summary")
    public List<Map<String, Object>> getDailySummary() {
        return habitService.getDailyHabitSummary();
    }

    @GetMapping("/daily-summary/{habitId}")
    public List<Map<String, Object>> getDailySummaryByHabit(@PathVariable Long habitId) {
        return habitService.getDailySummaryForHabit(habitId);
    }

    @GetMapping("/{id}/monthly-count")
    public int getMonthlyCount(
            @PathVariable Long id,
            @RequestParam int month,
            @RequestParam int year) {
        return habitService.getHabitMonthlyCount(id, month, year);
    }

}
