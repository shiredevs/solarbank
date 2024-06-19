import React, { JSX } from 'react';
import './LandingPage.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { NavigateFunction, useNavigate } from 'react-router-dom';

const LandingPage = (): JSX.Element => {
  const navigate: NavigateFunction = useNavigate();

  return (
    <div className="landing-page">
        <FontAwesomeIcon
          className='icon'
          icon={['fas', 'cloud-sun']}
          data-testid='landing-page-icon'
        />
        <p>Welcome to Solarbank</p>
      <button
        className='button'
        data-testid='landing-page-button'
        onClick={() => navigate('/form')}
      >
        begin
      </button>
    </div>
  );
}

export default LandingPage;
