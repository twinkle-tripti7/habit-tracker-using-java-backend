import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function AddHabit() {
    const [name, setName] = useState('');
    const [frequency, setFrequency] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.post('http://localhost:8080/api/habits', { name, frequency })
            .then(() => navigate('/'))
            .catch(err => console.error(err));
    };

    return (
        <form onSubmit={handleSubmit} className="p-4">
            <h2>Add Habit</h2>
            <input value={name} onChange={(e) => setName(e.target.value)} placeholder="Habit Name" required />
            <input value={frequency} onChange={(e) => setFrequency(e.target.value)} placeholder="Frequency" required />
            <button type="submit">Add</button>
        </form>
    );
}

export default AddHabit;