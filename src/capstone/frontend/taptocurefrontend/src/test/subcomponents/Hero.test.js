import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import { BrowserRouter as Router } from 'react-router-dom';
import Hero from '../../components/Subcomponents/Hero';
import Navbar from '../../components/Subcomponents/Navbar';
import '../../css/Hero.css'; // This is the missing line

// Mock the Navbar component
jest.mock('../../components/Subcomponents/Navbar', () => () => <div>Mocked Navbar</div>);

describe('Hero Component', () => {
  test('renders Hero component', () => {
    render(
      <Router>
        <Hero />
      </Router>
    );

    expect(screen.getByText('Care Just a Click Away')).toBeInTheDocument();
    expect(screen.getByText('Hassle-Free Healthcare Starts Here')).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /Book Appointment/i })).toBeInTheDocument();
  });

  test('renders Navbar component', () => {
    render(
      <Router>
        <Hero />
      </Router>
    );

    expect(screen.getByText('Mocked Navbar')).toBeInTheDocument();
  });

  test('has correct link for booking appointment', () => {
    render(
      <Router>
        <Hero />
      </Router>
    );

    const link = screen.getByRole('link', { name: /Book Appointment/i });
    expect(link).toHaveAttribute('href', '/patienthome');
  });
});
