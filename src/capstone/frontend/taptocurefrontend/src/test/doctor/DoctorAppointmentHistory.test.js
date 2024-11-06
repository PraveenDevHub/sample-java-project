import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import configureStore from 'redux-mock-store';
import DoctorAppointmentHistory from '../../components/Doctor/DoctorAppointmentHistory';
import axios from 'axios';
import { MemoryRouter } from 'react-router-dom';
import Navbar from '../../components/Subcomponents/Navbar';
import Footer from '../../components/Subcomponents/Footer';

jest.mock('axios');
jest.mock('../../components/Subcomponents/Navbar', () => () => <div>Navbar Component</div>);
jest.mock('../../components/Subcomponents/Footer', () => () => <div>Footer Component</div>);

const mockStore = configureStore([]);

describe('DoctorAppointmentHistory Component', () => {
    let store;

    beforeEach(() => {
        store = mockStore({
            auth: {
                token: 'test-token',
                doctorId: 'test-doctor-id',
            },
        });
    });

    test('renders without crashing', () => {
        render(
            <Provider store={store}>
                <MemoryRouter>
                    <DoctorAppointmentHistory />
                </MemoryRouter>
            </Provider>
        );
    });

    test('renders initial state correctly', () => {
        render(
            <Provider store={store}>
                <MemoryRouter>
                    <DoctorAppointmentHistory />
                </MemoryRouter>
            </Provider>
        );

        expect(screen.getByText('Appointment History')).toBeInTheDocument();
        expect(screen.getByPlaceholderText('Search by patient name')).toBeInTheDocument();
    });

    test('fetches appointment history correctly', async () => {
        axios.get.mockResolvedValue({
            data: [
                {
                    appointmentId: 1,
                    patient: { patientId: '1', user: { name: 'John Doe' } },
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
                    <DoctorAppointmentHistory />
                </MemoryRouter>
            </Provider>
        );

        await waitFor(() => {
            expect(screen.getByText('John Doe')).toBeInTheDocument();
        });
    });

    test('filters appointments by date and search term', async () => {
        axios.get.mockResolvedValue({
            data: [
                {
                    appointmentId: 1,
                    patient: { patientId: '1', user: { name: 'John Doe' } },
                    appointmentDate: '2023-10-01',
                    appointmentTimeSlot: '10:00',
                    reasonForVisit: 'Checkup',
                    status: 'Completed',
                },
                {
                    appointmentId: 2,
                    patient: { patientId: '2', user: { name: 'Jane Smith' } },
                    appointmentDate: '2023-10-02',
                    appointmentTimeSlot: '11:00',
                    reasonForVisit: 'Consultation',
                    status: 'Pending',
                },
            ],
        });

        render(
            <Provider store={store}>
                <MemoryRouter>
                    <DoctorAppointmentHistory />
                </MemoryRouter>
            </Provider>
        );

        await waitFor(() => {
            expect(screen.getByText('John Doe')).toBeInTheDocument();
            expect(screen.getByText('Jane Smith')).toBeInTheDocument();
        });

        fireEvent.change(screen.getByPlaceholderText('Search by patient name'), { target: { value: 'John' } });
        expect(screen.queryByText('Jane Smith')).not.toBeInTheDocument();

        fireEvent.change(screen.getByPlaceholderText('Search by patient name'), { target: { value: '' } });
        fireEvent.change(screen.getByRole('textbox', { type: 'date' }), { target: { value: '2023-10-01' } });
        expect(screen.queryByText('Jane Smith')).not.toBeInTheDocument();
    });

    test('handles display all correctly', async () => {
        axios.get.mockResolvedValue({
            data: [
                {
                    appointmentId: 1,
                    patient: { patientId: '1', user: { name: 'John Doe' } },
                    appointmentDate: '2023-10-01',
                    appointmentTimeSlot: '10:00',
                    reasonForVisit: 'Checkup',
                    status: 'Completed',
                },
                {
                    appointmentId: 2,
                    patient: { patientId: '2', user: { name: 'Jane Smith' } },
                    appointmentDate: '2023-10-02',
                    appointmentTimeSlot: '11:00',
                    reasonForVisit: 'Consultation',
                    status: 'Pending',
                },
            ],
        });

        render(
            <Provider store={store}>
                <MemoryRouter>
                    <DoctorAppointmentHistory />
                </MemoryRouter>
            </Provider>
        );

        await waitFor(() => {
            expect(screen.getByText('John Doe')).toBeInTheDocument();
            expect(screen.getByText('Jane Smith')).toBeInTheDocument();
        });

        fireEvent.change(screen.getByPlaceholderText('Search by patient name'), { target: { value: 'John' } });
        expect(screen.queryByText('Jane Smith')).not.toBeInTheDocument();

        fireEvent.click(screen.getByText('Display All'));
        expect(screen.getByText('Jane Smith')).toBeInTheDocument();
    });

    // test('handles error state correctly', async () => {
    //     store = mockStore({
    //         auth: {
    //             token: 'test-token',
    //             doctorId: null,
    //         },
    //     });

    //     render(
    //         <Provider store={store}>
    //             <MemoryRouter>
    //                 <DoctorAppointmentHistory />
    //             </MemoryRouter>
    //         </Provider>
    //     );

    //     await waitFor(() => {
    //         expect(screen.getByText((content, element) => content.includes('Doctor ID not found'))).toBeInTheDocument();
    //     });
    // });
});