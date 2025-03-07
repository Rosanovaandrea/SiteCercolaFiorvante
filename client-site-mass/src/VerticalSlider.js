import React, { useState } from 'react';
import './VerticalSlider.css'; // Importa il file CSS

function VerticalSlider() {
  const slides = [
    {
      title: 'Chi Sono',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur..',
    },
    {
      title: 'Le mie Qualifiche',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur..',
    }
  ];

  const [currentSlide, setCurrentSlide] = useState(0);

  const handleSlideChange = (index) => {
    setCurrentSlide(index);
  };

  return (
    <div className="vertical-slider">
      <div className="menu">
        {slides.map((slide, index) => (
          <button
            key={index}
            className={index === currentSlide ? 'active' : ''}
            onClick={() => handleSlideChange(index)}
          >
            {slide.title}
          </button>
        ))}
      </div>
      <div className="content">
        <h2>{slides[currentSlide].title}</h2>
        <p>{slides[currentSlide].description}</p>
      </div>
    </div>
  );
}

export default VerticalSlider;