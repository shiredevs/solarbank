import { JSX } from 'react';
import style from './Icon.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import applicationIcons from '../utils/IconLibrary';
import { IconDefinition } from '@fortawesome/free-solid-svg-icons';

type IconProps = {
  iconRef: IconDefinition;
};

const isValid = (icon: IconDefinition): boolean => {
  return Object.values(applicationIcons).includes(icon);
};

const Icon = (props: IconProps): JSX.Element => {
  const { iconRef } = props;
  const fallbackIcon: IconDefinition = applicationIcons.FALLBACK;

  return (
    <FontAwesomeIcon
      icon={isValid(iconRef) ? iconRef : fallbackIcon}
      className={style.icon}
      aria-hidden={false}
    />
  );
};

export default Icon;
