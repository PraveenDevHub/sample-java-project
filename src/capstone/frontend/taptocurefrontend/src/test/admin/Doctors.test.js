import React from 'react';
import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import '@testing-library/jest-dom';
import axios from 'axios';
import { Provider } from 'react-redux';
import configureStore from 'redux-mock-store';
import Doctors from '../../components/Admin/Doctors';
import { BrowserRouter as Router } from 'react-router-dom';

// Mock axios
jest.mock('axios');

// Mock store
const mockStore = configureStore([]);
const store = mockStore({
  auth: {
    token: 'test-token',
  },
});

// Mock window.location
const originalLocation = window.location;
delete window.location;
window.location = { ...originalLocation, href: jest.fn() };

// Mock console.error
beforeAll(() => {
    jest.spyOn(console, 'error').mockImplementation(() => {});
  });
  
  afterAll(() => {
    console.error.mockRestore();
  });

describe('Doctors', () => {
  beforeEach(() => {
    axios.get.mockResolvedValue({
      data: [
        {
          doctorId: 1,
          name: 'Dr. John Doe',
          medicalLicenseNumber: 'ABC123',
          email: 'john.doe@example.com',
          specialization: 'Cardiology',
          yearsOfExperience: 10,
          hospitalName: 'City Hospital'
        },
        {
          doctorId: 2,
          name: 'Dr. Jane Smith',
          medicalLicenseNumber: 'XYZ456',
          email: 'jane.smith@example.com',
          specialization: 'Neurology',
          yearsOfExperience: 8,
          hospitalName: 'County Hospital'
        }
      ]
    });
  });

  test('renders Doctors component', async () => {
    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <Doctors />
          </Router>
        </Provider>
      );
    });

    expect(screen.getByText('Admin Panel')).toBeInTheDocument();
    expect(screen.getByText('Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Patients')).toBeInTheDocument();
    expect(screen.getByText('Doctors')).toBeInTheDocument();
    expect(screen.getAllByText('Pending Approvals').length).toBeGreaterThan(0);
    expect(screen.getByText('Statistics')).toBeInTheDocument();
    expect(screen.getByText('Doctors List')).toBeInTheDocument();
  });

  test('fetches and displays doctors', async () => {
    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <Doctors />
          </Router>
        </Provider>
      );
    });

    await waitFor(() => {
      expect(screen.getByText('Dr. John Doe')).toBeInTheDocument();
      expect(screen.getByText('Dr. Jane Smith')).toBeInTheDocument();
    });
  });

  test('filters doctors by license number', async () => {
    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <Doctors />
          </Router>
        </Provider>
      );
    });

    fireEvent.change(screen.getByPlaceholderText('Search by License Number'), { target: { value: 'ABC123' } });

    await waitFor(() => {
      expect(screen.getByText('Dr. John Doe')).toBeInTheDocument();
      expect(screen.queryByText('Dr. Jane Smith')).not.toBeInTheDocument();
    });
  });

  test('filters doctors by approval status', async () => {
    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <Doctors />
          </Router>
        </Provider>
      );
    });

    fireEvent.change(screen.getByLabelText('Filter by Approval Status:'), { target: { value: 'Rejected' } });

    await waitFor(() => {
      expect(axios.get).toHaveBeenCalledWith('http://localhost:8091/doctorsdisplay/doctors?approvalStatus=Rejected', expect.any(Object));
    });
  });

  test('handles error when fetching doctors', async () => {
    axios.get.mockRejectedValueOnce(new Error('Error fetching doctors'));

    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <Doctors />
          </Router>
        </Provider>
      );
    });

    await waitFor(() => {
      expect(console.error).toHaveBeenCalledWith('Error fetching doctors:', expect.any(Error));
    });
  });

  test('navigates to correct pages on sidebar click', async () => {
    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <Doctors />
          </Router>
        </Provider>
      );
    });

    fireEvent.click(screen.getByText('Dashboard'));
    expect(window.location.href).toContain('/adminhome');

    fireEvent.click(screen.getByText('Patients'));
    expect(window.location.href).toContain('/patients');

    fireEvent.click(screen.getByText('Doctors'));
    expect(window.location.href).toContain('/doctors');

    fireEvent.click(screen.getAllByText('Pending Approvals')[0]);
    expect(window.location.href).toContain('/pending-approvals');

    fireEvent.click(screen.getByText('Statistics'));
    expect(window.location.href).toContain('/statistics');
  });
});
