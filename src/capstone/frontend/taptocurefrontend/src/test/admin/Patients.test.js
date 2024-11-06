import React from 'react';
import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import '@testing-library/jest-dom';
import axios from 'axios';
import { Provider } from 'react-redux';
import configureStore from 'redux-mock-store';
import Patients from '../../components/Admin/Patients';
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

describe('Patients', () => {
  beforeEach(() => {
    axios.get.mockResolvedValue({
      data: [
        {
          patientId: 1,
          name: 'John Doe',
          email: 'john.doe@example.com',
          age: 30,
          gender: 'Male'
        },
        {
          patientId: 2,
          name: 'Jane Smith',
          email: 'jane.smith@example.com',
          age: 25,
          gender: 'Female'
        }
      ]
    });
  });

  test('renders Patients component', async () => {
    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <Patients />
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
    expect(screen.getByText('Patients List')).toBeInTheDocument();
  });

  test('fetches and displays patients', async () => {
    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <Patients />
          </Router>
        </Provider>
      );
    });

    await waitFor(() => {
      expect(screen.getByText('John Doe')).toBeInTheDocument();
      expect(screen.getByText('Jane Smith')).toBeInTheDocument();
    });
  });

  test('filters patients by email', async () => {
    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <Patients />
          </Router>
        </Provider>
      );
    });

    fireEvent.change(screen.getByPlaceholderText('Search by email...'), { target: { value: 'john.doe@example.com' } });

    await waitFor(() => {
      expect(screen.getByText('John Doe')).toBeInTheDocument();
      expect(screen.queryByText('Jane Smith')).not.toBeInTheDocument();
    });
  });

  test('handles error when fetching patients', async () => {
    axios.get.mockRejectedValueOnce(new Error('Error fetching patients'));

    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <Patients />
          </Router>
        </Provider>
      );
    });

    await waitFor(() => {
      expect(console.error).toHaveBeenCalledWith('Error fetching patients:', expect.any(Error));
    });
  });

  test('navigates to correct pages on sidebar click', async () => {
    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <Patients />
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
