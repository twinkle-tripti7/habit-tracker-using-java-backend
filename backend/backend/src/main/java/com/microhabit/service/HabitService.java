package com.microhabit.service;

import com.microhabit.model.Habit;
import com.microhabit.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;

@Service
public class HabitService {

    @Autowired
    private HabitRepository habitRepository;

    public List<Habit> getAllHabits() {
        return habitRepository.findAll();
    }

    public Optional<Habit> getHabitById(Long id) {
        return habitRepository.findById(id);
    }

    public Habit updateHabit(Long id, Habit updatedHabit) {
        return habitRepository.findById(id)
                .map(habit -> {
                    habit.setTitle(updatedHabit.getTitle());
                    habit.setDescription(updatedHabit.getDescription());
                    habit.setCompleted(updatedHabit.isCompleted());
                    return habitRepository.save(habit);
                }).orElse(null);
    }

    public void deleteHabit(Long id) {
        habitRepository.deleteById(id);
    }

    public Habit toggleHabitCompletion(Long id) {
        return habitRepository.findById(id).map(habit -> {
            habit.setCompleted(!habit.isCompleted());
            return habitRepository.save(habit);
        }).orElse(null);
    }

    public Habit addHabit(Habit habit) {
        habit.setCreatedDate(LocalDate.now());
        return habitRepository.save(habit);
    }

    public Map<LocalDate, Long> getDailyHabitSummary() {
        return habitRepository.findAll().stream()
                .collect(Collectors.groupingBy(Habit::getCreatedDate, Collectors.counting()));
    }

}
