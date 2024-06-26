import React, { JSX } from 'react';
import style from './Button.module.css';

type ButtonProps = {
  handleClick: () => void;
  label: string;
}

const Button = (props: ButtonProps): JSX.Element => {
  const {handleClick, label} = props;

  return (
    <button className={style.button} onClick={handleClick}>
      {label}
    </button>
  )
}

export default Button;
