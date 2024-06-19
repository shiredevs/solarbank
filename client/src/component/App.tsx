import React from 'react';
import './App.css';
import '../icon/IconLibrary';
import router from '../router/AppRouter';
import { RouterProvider } from 'react-router-dom';

const App = () => {
  return(
    <div>
     <RouterProvider router={router} />
    </div>
  )
}

export default App;
