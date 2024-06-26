import React from 'react';
import { render, screen, cleanup } from '@testing-library/react';
import Icon from './Icon';
import applicationIcons from '../utils/IconLibrary';
import { IconDefinition, IconName } from '@fortawesome/free-solid-svg-icons';

describe('icon tests', () => {
  it('renders every app icon when valid reference provided', () => {
    const iconRefs: IconDefinition[] = applicationIcons;

    iconRefs.forEach((iconRef: IconDefinition) => {
      render(<Icon iconRef={[iconRef.prefix, iconRef.iconName]} />);

      const icon: HTMLElement = screen.getByRole('img');
      expect(icon).toHaveClass('icon');
      expect(icon.getAttribute('data-icon')).toBe(iconRef.iconName);

      cleanup();
    });
  });

  it('renders fallback icon when invalid reference provided', () => {
    const invalidIconRef: IconName = 'invalid-icon-name' as IconName;
    const fallbackIconRef: IconDefinition = applicationIcons[0];

    render(<Icon iconRef={['fas', invalidIconRef]} />);

    const icon: HTMLElement = screen.getByRole('img');
    expect(icon).toHaveClass('icon');
    expect(icon.getAttribute('data-icon')).toBe(fallbackIconRef.iconName);
  });
});
