import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import Feature from '../../components/Subcomponents/Feauture';
import FeatureBox from '../../components/Subcomponents/FeatureBox';

// Mock the FeatureBox component
jest.mock('../../components/Subcomponents/FeatureBox', () => ({ icon, title, description }) => (
  <div data-testid="feature-box">
    <span>{icon}</span>
    <h3>{title}</h3>
    <p>{description}</p>
  </div>
));

describe('Feature', () => {
  test('renders Feature component', () => {
    render(<Feature />);

    // Check if the heading is rendered
    expect(screen.getByText('Features')).toBeInTheDocument();

    // Check if all FeatureBox components are rendered
    const featureBoxes = screen.getAllByTestId('feature-box');
    expect(featureBoxes).toHaveLength(3);

    // Check the content of each FeatureBox
    expect(screen.getByText('🔍')).toBeInTheDocument();
    expect(screen.getByText('Quick Specialist Search')).toBeInTheDocument();
    expect(screen.getByText('Find doctors by specialty, location, hospital in seconds.')).toBeInTheDocument();

    expect(screen.getByText('🔄')).toBeInTheDocument();
    expect(screen.getByText('Easy Rescheduling & Cancellations')).toBeInTheDocument();
    expect(screen.getByText('Flexibly adjust or cancel appointments with just a few clicks.')).toBeInTheDocument();

    expect(screen.getByText('🕒')).toBeInTheDocument();
    expect(screen.getByText('Timely Appointment Reminders')).toBeInTheDocument();
    expect(screen.getByText('Receive automatic notifications to ensure you never miss an appointment.')).toBeInTheDocument();
  });
});
