import React, { JSX } from 'react';
import '../styles/SplashScreen.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const LandingPage = (): JSX.Element => {
  return (
    <div className="App-header">
        <FontAwesomeIcon
          className="size-40"
          icon={['fas', 'cloud-sun']}
          data-testid="landing-page-icon"
        />
        <p>Welcome to Solarbank</p>
      <button
        className="bg-orange-800 text-white font-bold py-2 px-4 border border-blue-700 rounded"
        data-testid="landing-page-button"
      >
        begin
      </button>
    </div>
  );
}

export default LandingPage;
