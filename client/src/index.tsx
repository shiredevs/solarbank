import React from 'react';
import ReactDOM from 'react-dom/client';
import './styles/index.css';
import SplashScreen from './components/SplashScreen';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
    <SplashScreen />
  </React.StrictMode>
);
