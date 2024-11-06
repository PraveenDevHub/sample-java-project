import React from 'react';
import { render, screen } from '@testing-library/react';
import WhyChooseUs from '../../components/Subcomponents/WhyChooseUs';

describe('WhyChooseUs Component', () => {
    test('renders without crashing', () => {
        render(<WhyChooseUs />);
    });

    test('renders title correctly', () => {
        render(<WhyChooseUs />);
        expect(screen.getByText('Why Choose TapToCure?')).toBeInTheDocument();
    });

    test('renders all sections correctly', () => {
        render(<WhyChooseUs />);
        expect(screen.getByText('User-Friendly Experience')).toBeInTheDocument();
        expect(screen.getByText('Wide Range of Specialists')).toBeInTheDocument();
        expect(screen.getByText('Instant Appointment Booking')).toBeInTheDocument();
        expect(screen.getByText('Secure Patient Data')).toBeInTheDocument();
        expect(screen.getByText('Personalized Health Management')).toBeInTheDocument();
        expect(screen.getByText('Trusted by Healthcare Professionals')).toBeInTheDocument();
    });

    test('renders icons correctly', () => {
        render(<WhyChooseUs />);
        expect(screen.getByTestId('fa-mobile-alt')).toBeInTheDocument();
        expect(screen.getByTestId('fa-user-md')).toBeInTheDocument();
        expect(screen.getByTestId('fa-calendar-check')).toBeInTheDocument();
        expect(screen.getByTestId('fa-shield-alt')).toBeInTheDocument();
        expect(screen.getByTestId('fa-heart')).toBeInTheDocument();
        expect(screen.getByTestId('fa-certificate')).toBeInTheDocument();
    });

    test('renders text content correctly', () => {
        render(<WhyChooseUs />);
        expect(screen.getByText(/Our mobile-first design ensures booking appointments/i)).toBeInTheDocument();
        expect(screen.getByText(/TapToCure connects you with a diverse network of healthcare professionals/i)).toBeInTheDocument();
        expect(screen.getByText(/Book appointments in real-time and get instant confirmation/i)).toBeInTheDocument();
        expect(screen.getByText(/We protect your health information with top-tier encryption/i)).toBeInTheDocument();
        expect(screen.getByText(/Manage your appointments, get reminders, and store prescriptions/i)).toBeInTheDocument();
        expect(screen.getByText(/Every doctor on our platform is certified and experienced/i)).toBeInTheDocument();
    });
});
