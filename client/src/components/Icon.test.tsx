import { cleanup, render, screen } from '@testing-library/react';
import Icon from './Icon';
import applicationIcons from '../utils/IconLibrary';
import { faSadCry, faXmark, IconDefinition } from '@fortawesome/free-solid-svg-icons';

describe('icon tests', () => {
  const fallbackIcon: IconDefinition = faXmark;

  it('renders every app icon when valid reference provided', () => {
    Object.values(applicationIcons).forEach((iconRef: IconDefinition) => {
      render(<Icon iconRef={iconRef} />);

      const icon: HTMLElement = screen.getByRole('img');
      expect(icon).toHaveClass('icon');
      expect(icon.getAttribute('data-icon')).toBe(iconRef.iconName);

      cleanup();
    });
  });

  it('renders fallback icon when icon definition is undefined', () => {
    render(<Icon iconRef={applicationIcons.invalidIconRef} />);

    const icon: HTMLElement = screen.getByRole('img');
    expect(icon).toHaveClass('icon');
    expect(icon.getAttribute('data-icon')).toBe(fallbackIcon.iconName);
  });

  it('renders fallback icon when icon definition is not in valid application icons map', () => {
    render(<Icon iconRef={faSadCry} />);

    const icon: HTMLElement = screen.getByRole('img');
    expect(icon).toHaveClass('icon');
    expect(icon.getAttribute('data-icon')).toBe(fallbackIcon.iconName);
  });
});
