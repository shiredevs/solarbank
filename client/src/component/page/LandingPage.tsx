import React, { JSX } from 'react';
import './LandingPage.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { NavigateFunction, useNavigate } from 'react-router-dom';

const LandingPage = (): JSX.Element => {
  const navigate: NavigateFunction = useNavigate();

  return (
    <div className='landing-page' role='landing-page-container'>
        <FontAwesomeIcon
          className='icon'
          icon={['fas', 'cloud-sun']}
          aria-hidden={false}
        />
        <p>Welcome to Solarbank</p>
      <button
        className='button'
        onClick={() => navigate('/form')}
      >
        begin
      </button>
    </div>
  );
}

export default LandingPage;
