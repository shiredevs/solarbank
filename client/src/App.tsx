import React from 'react';
import './assets/styles/global.css';
import './utils/IconLibrary';
import { RouterProvider } from 'react-router-dom';
import { Router } from '@remix-run/router';

type AppProps = {
  router: Router;
};

const App = (props: AppProps) => {
  const { router } = props;

  return (
    <div>
      <RouterProvider router={router} />
    </div>
  );
};

export default App;
