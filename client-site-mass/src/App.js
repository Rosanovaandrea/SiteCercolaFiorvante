import logo from './logo.svg';
import './App.css';
import Calendar from './Calendar'
import Slider from './Slider';
import VerticalSlider from './VerticalSlider';

function App() {
  
  return (
    <div className="App">
      <header className="App-header">
      <h1>Fioravante Cercola Massaggiatore</h1>
      </header>
      <VerticalSlider/>
      <Slider/>
      <Calendar/>
    </div>
  );
}

export default App;
