import React from 'react';
import {render, screen, fireEvent} from '@testing-library/react';
import Button from './Button';

describe('button tests', () => {
  it('renders button with correct label', () => {
    const expectedLabel = 'label';

    render(<Button label={expectedLabel} handleClick={() => {}}/>)

    const button = screen.getByRole('button');
    expect(button).toHaveTextContent(expectedLabel);
  });

  it('handles click event when a handler is provided', () => {
    let eventFired = false;
    const clickHandler = () => eventFired = true;

    render(<Button handleClick={clickHandler} label={'test'} />);
    const button = screen.getByRole('button');
    fireEvent.click(button);

    expect(eventFired).toBeTruthy();
  });
})
