package com.microhabit.service;

import com.microhabit.dto.HabitDTO;
import com.microhabit.model.Habit;
import com.microhabit.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Collections;

@Service
public class HabitService {

    @Autowired
    private HabitRepository habitRepository;

    public Optional<Habit> getHabitById(Long id) {
        return habitRepository.findById(id);
    }

    public Habit updateHabit(Long id, Habit updatedHabit) {
        return habitRepository.findById(id)
                .map(habit -> {
                    habit.setTitle(updatedHabit.getTitle());
                    habit.setDescription(updatedHabit.getDescription());
                    return habitRepository.save(habit);
                }).orElse(null);
    }

    public void deleteHabit(Long id) {
        habitRepository.deleteById(id);
    }

    public Habit toggleHabitCompletion(Long id) {
        return habitRepository.findById(id).map(habit -> {
            String today = LocalDate.now().toString();
            boolean currentStatus = habit.getCompletionMap().getOrDefault(today, false);
            habit.getCompletionMap().put(today, !currentStatus);
            return habitRepository.save(habit);
        }).orElse(null);
    }

    public Habit createHabit(HabitDTO habitDto) {
        Habit habit = new Habit();
        habit.setTitle(habitDto.getTitle());
        habit.setDescription(habitDto.getDescription());
        habit.setCreatedDate(LocalDate.now());
        habit.setCompletionMap(new HashMap<>());
        return habitRepository.save(habit);
    }

    public List<Map<String, Object>> getDailyHabitSummary() {
        Map<String, Long> summary = new HashMap<>();

        for (Habit habit : habitRepository.findAll()) {
            habit.getCompletionMap().forEach((date, completed) -> {
                if (completed) {
                    summary.put(date, summary.getOrDefault(date, 0L) + 1);
                }
            });
        }

        // Convert to list of maps expected by frontend
        return summary.entrySet().stream().map(entry -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", entry.getKey());
            map.put("count", entry.getValue());
            return map;
        }).collect(Collectors.toList());
    }

    public List<HabitDTO> getAllHabits() {
        List<Habit> habits = habitRepository.findAll();
        String today = LocalDate.now().toString();

        return habits.stream().map(habit -> {
            HabitDTO dto = new HabitDTO(null, today, today, false);
            dto.setId(habit.getId());
            dto.setTitle(habit.getTitle());
            dto.setDescription(habit.getDescription());
            dto.setCompletedToday(habit.getCompletionMap().getOrDefault(today, false));
            return dto;
        }).collect(Collectors.toList());
    }

    // Mark completion for a date
    public Habit markCompleted(Long id, String date, boolean status) {
        return habitRepository.findById(id).map(habit -> {
            habit.getCompletionMap().put(date, status);
            return habitRepository.save(habit);
        }).orElse(null);
    }

    public List<Map<String, Object>> getDailySummaryForHabit(Long id) {
        return habitRepository.findById(id)
                .map(habit -> habit.getCompletionMap().entrySet().stream()
                        .filter(e -> e.getValue()) // only completed days
                        .map(e -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("date", e.getKey());
                            map.put("count", 1); // always 1 for individual habit
                            return map;
                        }).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    // count the no of times a particular habit is done in a month

    public int getHabitMonthlyCount(Long id, int month, int year) {
        return habitRepository.findById(id).map(habit -> (int) habit.getCompletionMap().entrySet().stream()
                .filter(e -> {
                    LocalDate d = LocalDate.parse(e.getKey());
                    return d.getMonthValue() == month &&
                            d.getYear() == year &&
                            e.getValue(); // ✅ check completed
                }).count()).orElse(0);
    }

}
