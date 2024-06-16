import React from 'react';
import ReactDOM from 'react-dom/client';
import './styles/index.css';
import './icons/IconLibrary';
import LandingPage from './components/LandingPage';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
    <LandingPage />
  </React.StrictMode>
);
