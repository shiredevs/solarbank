import React, { StrictMode } from 'react';
import ReactDOM from 'react-dom/client';
import App from './component/App';

const root : ReactDOM.Root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

root.render(
  <StrictMode>
    <App />
  </StrictMode>
);
