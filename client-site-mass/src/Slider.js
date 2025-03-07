import React from 'react';
import { Carousel } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css'; // Importa il CSS di Bootstrap
import './Slider.css'

function Slider() {
    const slides = [
      {
        image: 'img/immagine1.jpg',
        title: 'Massaggio orientale',
        text: 'Il massaggio orientale è un esperienza che va oltre il semplice rilassamento muscolare. Originario di antiche tradizioni asiatiche, si concentra sull equilibrio energetico del corpo e della mente Caratteristiche principali: Approccio olistico: Considera la persona nella sua interezza, non solo il corpo fisico. Punti energetici: Lavora sui meridiani e sui punti di pressione per ripristinare il flusso di energia vitale (Qi). Tecniche varie: Include digitopressione, stiramenti, manipolazioni e l uso di oli essenziali. Benefici: Riduce lo stress, allevia le tensioni muscolari, migliora la circolazione e promuove il benessere generale.',
      },
      {
        image: 'img/immagine2.jpg',
        title: 'massaggio rigenerante',
        text: 'Il massaggio orientale è un esperienza che va oltre il semplice rilassamento muscolare. Originario di antiche tradizioni asiatiche, si concentra sull equilibrio energetico del corpo e della mente Caratteristiche principali: Approccio olistico: Considera la persona nella sua interezza, non solo il corpo fisico. Punti energetici: Lavora sui meridiani e sui punti di pressione per ripristinare il flusso di energia vitale (Qi). Tecniche varie: Include digitopressione, stiramenti, manipolazioni e l uso di oli essenziali. Benefici: Riduce lo stress, allevia le tensioni muscolari, migliora la circolazione e promuove il benessere generale.',
      },
      {
        image: 'img/immagine3.jpeg',
        title: 'Titolo Slide 3',
        text: 'Il massaggio orientale è un esperienza che va oltre il semplice rilassamento muscolare. Originario di antiche tradizioni asiatiche, si concentra sull equilibrio energetico del corpo e della mente Caratteristiche principali: Approccio olistico: Considera la persona nella sua interezza, non solo il corpo fisico. Punti energetici: Lavora sui meridiani e sui punti di pressione per ripristinare il flusso di energia vitale (Qi). Tecniche varie: Include digitopressione, stiramenti, manipolazioni e l uso di oli essenziali. Benefici: Riduce lo stress, allevia le tensioni muscolari, migliora la circolazione e promuove il benessere generale.'
      },
    ];
  
    return (
      <Carousel>
        {slides.map((slide, index) => (
          <Carousel.Item key={index}>
            <div
              className="carousel-image"
              style={{ backgroundImage: `url(${slide.image})` }}
            />
            <Carousel.Caption>
              <h3>{slide.title}</h3>
              <p>{slide.text}</p>
            </Carousel.Caption>
          </Carousel.Item>
        ))}
      </Carousel>
    );
  }

  export default Slider;