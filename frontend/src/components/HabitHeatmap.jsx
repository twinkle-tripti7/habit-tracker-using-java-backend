import React, { useEffect, useState } from 'react';
import CalendarHeatmap from 'react-calendar-heatmap';
import 'react-calendar-heatmap/dist/styles.css';
import './heatmapStyles.css'; // Optional for custom colors

const HabitHeatmap = () => {
    const [summaryData, setSummaryData] = useState([]);

    useEffect(() => {
        fetch('http://localhost:8000/api/habits/daily-summary')
            .then((res) => res.json())
            .then((data) => {
                const formattedData = Object.entries(data).map(([date, count]) => ({
                    date,
                    count,
                }));
                setSummaryData(formattedData);
            });
    }, []);

    return (
        <div>
            <h3>📊 Habit Heatmap</h3>
            <div style={{ maxWidth: '30%', overflowX: 'auto' }}>
                <CalendarHeatmap
                    startDate={new Date('2025-07-01')}
                    endDate={new Date()}
                    values={summaryData}
                    classForValue={(value) => {
                        if (!value || value.count === 0) return 'color-empty';
                        if (value.count < 2) return 'color-scale-1';
                        if (value.count < 4) return 'color-scale-2';
                        return 'color-scale-3';
                    }}
                    showWeekdayLabels={true}
                />
            </div>

        </div>
    );
};

export default HabitHeatmap;
