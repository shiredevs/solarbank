import React, { JSX } from 'react';
import style from './LandingPage.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { NavigateFunction, useNavigate } from 'react-router-dom';
import Button from '../components/Button';
import { ROUTE_PATHS } from '../components/router/AppRoutes';

const LandingPage = (): JSX.Element => {
  const navigate: NavigateFunction = useNavigate();

  return (
    <div className={style.landingPage} role="landing-page-container">
      <FontAwesomeIcon className={style.icon} icon={['fas', 'cloud-sun']} aria-hidden={false} />
      <h1 className={style.heading}>Solarbank</h1>
      <p className={style.paragraph} aria-label='description'>estimate your solar energy savings</p>
      <Button handleClick={() => navigate(ROUTE_PATHS.FORM)} label={'begin'} />
    </div>
  );
};

export default LandingPage;
