import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import Landing from '../components/Landing';
import Hero from '../components/Subcomponents/Hero';
import Feature from '../components/Subcomponents/Feauture';
import About from '../components/Subcomponents/About';
import Footer from '../components/Subcomponents/Footer';
import WhyChooseUs from '../components/Subcomponents/WhyChooseUs';

jest.mock('../components/Subcomponents/Hero', () => () => <div>Hero Component</div>);
jest.mock('../components/Subcomponents/Feauture', () => () => <div id="features">Feature Component</div>);
jest.mock('../components/Subcomponents/About', () => () => <div id="about">About Component</div>);
jest.mock('../components/Subcomponents/Footer', () => () => <div>Footer Component</div>);
jest.mock('../components/Subcomponents/WhyChooseUs', () => () => <div>WhyChooseUs Component</div>);

describe('Landing Component', () => {
    test('renders without crashing', () => {
        render(
            <MemoryRouter>
                <Landing />
            </MemoryRouter>
        );
    });

    test('renders all subcomponents correctly', () => {
        render(
            <MemoryRouter>
                <Landing />
            </MemoryRouter>
        );

        expect(screen.getByText('Hero Component')).toBeInTheDocument();
        expect(screen.getByText('Feature Component')).toBeInTheDocument();
        expect(screen.getByText('WhyChooseUs Component')).toBeInTheDocument();
        expect(screen.getByText('About Component')).toBeInTheDocument();
        expect(screen.getByText('Footer Component')).toBeInTheDocument();
    });

    test('scrolls to the correct element when a hash is present in the URL', async () => {
        const scrollIntoViewMock = jest.fn();
        HTMLElement.prototype.scrollIntoView = scrollIntoViewMock;

        render(
            <MemoryRouter initialEntries={['/#features']}>
                <Landing />
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(scrollIntoViewMock).toHaveBeenCalled();
        });
    });
});