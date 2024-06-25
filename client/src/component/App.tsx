import React from 'react';
import './App.css';
import '../icon/IconLibrary';
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
