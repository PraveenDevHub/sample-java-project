import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import configureStore from 'redux-mock-store';
import AppointmentHistory from '../../components/Patient/AppointmentHistory';
import axios from 'axios';
import { MemoryRouter } from 'react-router-dom';

jest.mock('axios');
jest.mock('../../components/Subcomponents/Navbar', () => () => <div>Navbar Component</div>);
jest.mock('../../components/Subcomponents/Footer', () => () => <div>Footer Component</div>);

const mockStore = configureStore([]);

describe('AppointmentHistory Component', () => {
    let store;

    beforeEach(() => {
        store = mockStore({
            auth: {
                token: 'test-token',
                patientId: 'test-patient-id',
            },
        });
    });

    test('renders without crashing', () => {
        render(
            <Provider store={store}>
                <MemoryRouter>
                    <AppointmentHistory />
                </MemoryRouter>
            </Provider>
        );
    });

    test('renders initial state correctly', () => {
        render(
            <Provider store={store}>
                <MemoryRouter>
                    <AppointmentHistory />
                </MemoryRouter>
            </Provider>
        );

        expect(screen.getByText('Appointment History')).toBeInTheDocument();
    });

    test('fetches appointment history correctly', async () => {
        axios.get.mockResolvedValue({
            data: [
                {
                    appointmentId: 1,
                    doctor: { user: { name: 'Dr. Smith' } },
                    appointmentDate: '2023-10-01',
                    appointmentTimeSlot: '10:00',
                    reasonForVisit: 'Checkup',
                    status: 'Completed',
                },
            ],
        });

        render(
            <Provider store={store}>
                <MemoryRouter>
                    <AppointmentHistory />
                </MemoryRouter>
            </Provider>
        );

        await waitFor(() => {
            expect(screen.getByText('Dr. Smith')).toBeInTheDocument();
        });
    });

    // test('handles error state correctly', async () => {
    //     render(
    //         <Provider store={store}>
    //             <MemoryRouter>
    //                 <AppointmentHistory />
    //             </MemoryRouter>
    //         </Provider>
    //     );

    //     await waitFor(() => {
    //         expect(screen.getByText((content, element) => content.includes('Doctor ID not found. Please log in again.'))).toBeInTheDocument();
    //     });
    // });
});
