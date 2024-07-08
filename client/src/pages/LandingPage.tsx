import React, { JSX } from 'react';
import style from './LandingPage.module.css';
import { NavigateFunction, useNavigate } from 'react-router-dom';
import Button from '../components/Button';
import { ROUTE_PATHS } from '../components/router/AppRoutes';
import Icon from '../components/Icon';
import applicationIcons from '../utils/IconLibrary';

const LandingPage = (): JSX.Element => {
  const navigate: NavigateFunction = useNavigate();

  return (
    <div className={style.landingPage} role="landing-page-container">
      <Icon iconRef={applicationIcons.LANDING_PAGE} />
      <h1 className={style.heading}>Solarbank</h1>
      <p className={style.paragraph}>estimate your solar energy savings</p>
      <Button handleClick={() => navigate(ROUTE_PATHS.FORM)} label={'begin'} />
    </div>
  );
};

export default LandingPage;
