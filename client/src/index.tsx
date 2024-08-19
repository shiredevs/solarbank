import { StrictMode } from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import router from './components/router/AppRouter';
import { config } from './clients/config/CalculateConfig';

const root: ReactDOM.Root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);

root.render(
  <StrictMode>
    <App router={router} calculateConfig={config} />
  </StrictMode>
);
