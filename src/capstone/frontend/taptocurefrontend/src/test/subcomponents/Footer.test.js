import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import Footer from '../../components/Subcomponents/Footer';

describe('Footer Component', () => {
    test('renders without crashing', () => {
        render(
            <Router>
                <Footer />
            </Router>
        );
    });

    test('renders all sections correctly', () => {
        render(
            <Router>
                <Footer />
            </Router>
        );

        expect(screen.getByText('TapToCure')).toBeInTheDocument();
        expect(screen.getByText('Quick Links')).toBeInTheDocument();
        expect(screen.getByText('Contact Us')).toBeInTheDocument();
        expect(screen.getByText('Follow Us')).toBeInTheDocument();
    });

    test('renders quick links with correct paths', () => {
        render(
            <Router>
                <Footer />
            </Router>
        );

        expect(screen.getByText('Home').closest('a')).toHaveAttribute('href', '/');
        expect(screen.getByText('Features').closest('a')).toHaveAttribute('href', '/#features');
        expect(screen.getByText('About').closest('a')).toHaveAttribute('href', '/#about');
    });

    test('renders contact information correctly', () => {
        render(
            <Router>
                <Footer />
            </Router>
        );

        expect(screen.getByText((content, element) => content.includes('support@taptocure.com'))).toBeInTheDocument();
        expect(screen.getByText((content, element) => content.includes('+123-456-7890'))).toBeInTheDocument();
        expect(screen.getByText((content, element) => content.includes('Electronic City, Bengaluru'))).toBeInTheDocument();
    });

    test('renders social media icons correctly', () => {
        render(
            <Router>
                <Footer />
            </Router>
        );

        const socialLinks = screen.getAllByRole('link', { name: '' }).filter(link => link.querySelector('i'));
        expect(socialLinks).toHaveLength(4);
        socialLinks.forEach(link => {
            expect(link).toHaveAttribute('href', '#');
        });
    });

    test('renders footer bottom with current year', () => {
        render(
            <Router>
                <Footer />
            </Router>
        );

        const currentYear = new Date().getFullYear();
        expect(screen.getByText(`© ${currentYear} TapToCure. All rights reserved.`)).toBeInTheDocument();
    });
});
