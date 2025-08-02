import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

function Home() {
    const [habits, setHabits] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/api/habits')
            .then(response => setHabits(response.data))
            .catch(err => console.error(err));
    }, []);

    return (
        <div className="p-4">
            <h1>My Habits</h1>
            <Link to="/add">Add New Habit</Link>
            <ul>
                {habits.map(habit => (
                    <li key={habit.id}>{habit.name} - {habit.frequency}</li>
                ))}
            </ul>
        </div>
    );
}

export default Home;
