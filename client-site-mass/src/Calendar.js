
import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import './Calendar.css'; // Importa il file CSS

function Calendar() {
  const [selectedDates, setSelectedDates] = useState([]);
  const [excludedDates, setExcludedDates] = useState([]);

  const handleDateChange = (dates) => {
    setSelectedDates(dates);
  };

  const handleExcludeDate = (date) => {
    if (excludedDates.includes(date)) {
      setExcludedDates(excludedDates.filter((d) => d !== date));
    } else {
      setExcludedDates([...excludedDates, date]);
    }
  };

  const isDateExcluded = (date) => {
    return excludedDates.some(
      (excludedDate) =>
        excludedDate.getDate() === date.getDate() &&
        excludedDate.getMonth() === date.getMonth() &&
        excludedDate.getFullYear() === date.getFullYear()
    );
  };

  return (
    <div className="calendar-container">
      <DatePicker
        selected={selectedDates[0]}
        onChange={handleDateChange}
        inline
        highlightDates={excludedDates.map((date) => ({
          'react-datepicker__day--excluded': isDateExcluded(date),
          date: date,
        }))}
        dayClassName={(date) =>
          isDateExcluded(date) ? 'excluded-date' : null
        }
        onDayMouseEnter={handleExcludeDate}
      />
    </div>
  );
}

export default Calendar;