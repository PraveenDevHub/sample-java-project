import React from 'react';
import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import '@testing-library/jest-dom';
import axios from 'axios';
import { Provider } from 'react-redux';
import configureStore from 'redux-mock-store';
import AdminDashboard from '../../components/Admin/AdminDashboard';
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

describe('AdminDashboard', () => {
  beforeEach(() => {
    axios.get.mockResolvedValue({
      data: {
        patients: 10,
        doctors: 5,
        approvals: 2,
      },
    });
  });

  test('renders AdminDashboard component', async () => {
    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <AdminDashboard />
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
  });

  test('fetches and displays counts', async () => {
    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <AdminDashboard />
          </Router>
        </Provider>
      );
    });

    await waitFor(() => {
      expect(screen.getByText('Total Patients')).toBeInTheDocument();
      expect(screen.getByText('10')).toBeInTheDocument();
      expect(screen.getByText('Total Doctors')).toBeInTheDocument();
      expect(screen.getByText('5')).toBeInTheDocument();
      expect(screen.getAllByText('Pending Approvals')[1]).toBeInTheDocument();
      expect(screen.getByText('2')).toBeInTheDocument();
    });
  });

  test('navigates to correct pages on sidebar click', async () => {
    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <AdminDashboard />
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

  test('navigates to correct pages on card click', async () => {
    await act(async () => {
      render(
        <Provider store={store}>
          <Router>
            <AdminDashboard />
          </Router>
        </Provider>
      );
    });

    fireEvent.click(screen.getByText('Total Patients'));
    expect(window.location.href).toContain('/patients');

    fireEvent.click(screen.getByText('Total Doctors'));
    expect(window.location.href).toContain('/doctors');

    fireEvent.click(screen.getAllByText('Pending Approvals')[1]);
    expect(window.location.href).toContain('/pending-approvals');

    fireEvent.click(screen.getByText('View Statistics'));
    expect(window.location.href).toContain('/statistics');
  });
});
