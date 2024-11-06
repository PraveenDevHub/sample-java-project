import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import About from '../../components/Subcomponents/About';
import aboutus from '../../assests/aboutus.jpg';

// Mock the image import
jest.mock('../../assests/aboutus.jpg', () => 'aboutus.jpg');

describe('About', () => {
  test('renders About component', () => {
    render(<About />);

    // Check if the heading is rendered
    expect(screen.getByText('About Us')).toBeInTheDocument();

    // Check if the image is rendered
    const image = screen.getByAltText('');
    expect(image).toBeInTheDocument();
    expect(image).toHaveAttribute('src', 'aboutus.jpg');

    // Check if the paragraph text is rendered
    expect(screen.getByText(/TapToCure is a seamless online doctor appointment booking platform/i)).toBeInTheDocument();
  });
});
