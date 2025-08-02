import React, { useEffect, useState } from 'react';
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

      {/* Include the heatmap here */}
      <HabitHeatmap />
    </div>
  );
}

export default App;
