
/*import React, { useEffect, useState } from 'react';
import axios from 'axios';
import HabitHeatmap from './components/HabitHeatmap';

function App() {
  const [habits, setHabits] = useState([]);
  const [title, setTitle] = useState('');

  const fetchHabits = () => {
    axios.get('http://localhost:8000/api/habits')
      .then(res => setHabits(res.data))
      .catch(err => console.error(err));
  };

  const addHabit = (e) => {
    e.preventDefault();
    if (title.trim() === '') return;

    axios.post('http://localhost:8000/api/habits', { title })
      .then(() => {
        setTitle('');
        fetchHabits();
      })
      .catch(err => console.error(err));
  };

  useEffect(() => {
    fetchHabits();
  }, []);

  return (
    <div style={{ padding: '2rem', fontFamily: 'Arial' }}>
      <h1>🌱 MicroHabit Tracker</h1>

      <form onSubmit={addHabit} style={{ marginBottom: '1rem' }}>
        <input
          type="text"
          placeholder="Enter habit..."
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          style={{ padding: '0.5rem', width: '300px' }}
        />
        <button type="submit" style={{ padding: '0.5rem 1rem', marginLeft: '10px' }}>
          Add Habit
        </button>
      </form>

      <ul>
        {habits.map(habit => (
          <li key={habit.id} style={{ marginBottom: '0.5rem' }}>{habit.title}</li>
        ))}
      </ul>

      <HabitHeatmap />
    </div>
  );
}

export default App;

*/





import React, { useEffect, useState, useCallback } from 'react';
import CalendarHeatmap from 'react-calendar-heatmap';
import 'react-calendar-heatmap/dist/styles.css';
import axios from 'axios';
import './components/heatmapStyles.css';



const months = [
  'January', 'February', 'March', 'April', 'May', 'June',
  'July', 'August', 'September', 'October', 'November', 'December'
];


function HabitTracker() {
  const [habitInput, setHabitInput] = useState('');
  const [habits, setHabits] = useState([]);
  const [heatmapData, setHeatmapData] = useState([]);
  const [selectedMonth, setSelectedMonth] = useState(new Date().toLocaleString('default', { month: 'long' }));
  const [selectedHabit, setSelectedHabit] = useState('');


  const today = new Date().toISOString().split('T')[0];

  const [habitMonthlyCount, setHabitMonthlyCount] = useState(null);


  const fetchHabits = useCallback(() => {
    axios.get('http://localhost:8000/api/habits')
      .then(res => setHabits(res.data))
      .catch(err => console.error(err));
  }, []);


  const fetchHeatmapData = useCallback(() => {
    const url = selectedHabit
      ? `http://localhost:8000/api/habits/daily-summary/${selectedHabit}`
      : `http://localhost:8000/api/habits/daily-summary`;

    axios.get(url)
      .then(res => {
        const monthIndex = months.indexOf(selectedMonth);
        const start = new Date(2025, monthIndex, 1);
        const end = new Date(2025, monthIndex + 1, 0);

        const filtered = res.data.filter(entry => {
          const date = new Date(entry.date);
          return date >= start && date <= end;
        });

        setHeatmapData(filtered);
      })
      .catch(err => console.error("Heatmap fetch error:", err));
  }, [selectedHabit, selectedMonth]);

  const fetchMonthlyCount = useCallback(() => {
    if (!selectedHabit) {
      setHabitMonthlyCount(null);
      return;
    }

    const selected = habits.find(h => h.title === selectedHabit);
    if (!selected) return;

    const month = new Date().getMonth() + 1;
    const year = new Date().getFullYear();

    axios.get(`http://localhost:8000/api/habits/${selected.id}/monthly-count?month=${month}&year=${year}`)
      .then(res => setHabitMonthlyCount(res.data))
      .catch(err => console.error("Monthly count fetch error", err));
  }, [selectedHabit, habits]);



  useEffect(() => {
    fetchHabits();
  }, [fetchHabits]); // ✅ Now ESLint warning will disappear

  useEffect(() => {
    fetchHeatmapData();
  }, [selectedMonth, fetchHeatmapData]);



  useEffect(() => {
    fetchMonthlyCount();
  }, [selectedHabit, habits, fetchMonthlyCount]);

  const addHabit = () => {
    if (!habitInput.trim()) return;
    axios.post('http://localhost:8000/api/habits', {
      title: habitInput,
      description: ''
    }).then(() => {
      setHabitInput('');
      fetchHabits();
    });
  };

  const toggleCompletion = (id) => {
    axios.put(`http://localhost:8000/api/habits/${id}/toggle`)
      .then(() => {
        fetchHabits();
        fetchHeatmapData();
      });
  };

  return (
    <div style={{ padding: '30px', fontFamily: 'Arial', maxWidth: '800px', margin: 'auto' }}>
      <h1>🌱 MicroHabit Tracker</h1>

      <div style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
        <input
          type="text"
          placeholder="Enter habit..."
          value={habitInput}
          onChange={(e) => setHabitInput(e.target.value)}
          style={{ flex: 1, padding: '8px' }}
        />
        <button onClick={addHabit} style={{ padding: '8px 16px' }}>Add Habit</button>
      </div>

      {/* ✅ Today's Habits */}
      <div>
        <h3>✅ Today's Habits</h3>
        {habits.filter(h => !h.completedToday).map(habit => (
          <div key={habit.id} style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
            <input type="checkbox" onChange={() => toggleCompletion(habit.id)} />
            <span style={{ marginLeft: '10px' }}>{habit.title}</span>
          </div>
        ))}
      </div>

      {/* Filters */}
      <div style={{ display: 'flex', gap: '10px', margin: '30px 0 10px' }}>
        <select value={selectedMonth} onChange={(e) => setSelectedMonth(e.target.value)}>
          {months.map(month => (
            <option key={month} value={month}>{month}</option>
          ))}
        </select>

        <select value={selectedHabit} onChange={(e) => setSelectedHabit(e.target.value)}>
          <option value="">All Habits</option>
          {habits.map(h => (
            <option key={h.id} value={h.title}>{h.title}</option>
          ))}
        </select>
      </div>

      {/* Monthly Count */}
      {selectedHabit && habitMonthlyCount !== null && (
        <p style={{ fontSize: '16px', marginTop: '10px' }}>
          📅 You performed habit "<strong>{selectedHabit}</strong>" on <strong>{habitMonthlyCount}</strong> days this month.
        </p>
      )}

      {/* Heatmap */}
      <h3>📊 Heatmap for {selectedHabit || "All Habits"} in {selectedMonth}</h3>
      <div style={{ width: '50%', overflowX: 'auto' }}>
        <div style={{ transform: 'scale(0.85)', transformOrigin: 'top right' }}>
          <CalendarHeatmap
            startDate={new Date(2025, months.indexOf(selectedMonth), 1)}
            endDate={new Date(2025, months.indexOf(selectedMonth) + 1, 0)}
            values={heatmapData}
            classForValue={(value) => {
              if (!value) return 'color-empty';
              if (value.count < 1) return 'color-empty';
              if (value.count === 1) return 'color-scale-1';
              if (value.count === 2) return 'color-scale-2';
              return 'color-scale-3';
            }}
            showWeekdayLabels
          />
        </div>
      </div>
    </div>
  );
}

export default HabitTracker;
