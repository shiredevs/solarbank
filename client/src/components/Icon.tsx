import React, { JSX } from 'react';
import style from './Icon.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IconPrefix } from '@fortawesome/fontawesome-svg-core';
import applicationIcons from '../utils/IconLibrary';
import { IconDefinition, IconName } from '@fortawesome/free-solid-svg-icons';

type IconProps = {
  iconRef: [IconPrefix, IconName];
};

const isValid = (name: string): boolean => {
  return applicationIcons.filter((validIcon): boolean => validIcon.iconName === name).length > 0;
};

const Icon = (props: IconProps): JSX.Element => {
  const { iconRef } = props;
  const fallbackIcon: IconDefinition = applicationIcons[0];

  return (
    <FontAwesomeIcon
      icon={isValid(iconRef[1]) ? iconRef : fallbackIcon}
      className={style.icon}
      aria-hidden={false}
    />
  );
};

export default Icon;
