import logo from './logo.svg';
import './App.css';
import Calendar from './Calendar'
import Slider from './Slider';
import VerticalSlider from './VerticalSlider';
import CustomerManager from './CustomerManager';

function App() {
  
  return (
    <div className="App">
      <header className="App-header">
      <h1>Fioravante Cercola Massaggiatore</h1>
      </header>
      <VerticalSlider/>
      <Slider/>
      <Calendar/>
      <CustomerManager/>
    </div>
  );
}

export default App;
