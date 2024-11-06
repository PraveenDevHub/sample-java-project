import React from 'react';
import { render, screen } from '@testing-library/react';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { Provider } from 'react-redux';
import configureStore from 'redux-mock-store';
import ProtectedRoute from '../../components/Auth/ProtectedRoute';

const mockStore = configureStore([]);

describe('ProtectedRoute', () => {
  let store;

  beforeEach(() => {
    store = mockStore({
      auth: {
        token: null,
        role: null,
      },
    });
  });

  const renderWithRouter = (ui, { route = '/' } = {}) => {
    window.history.pushState({}, 'Test page', route);

    return render(
      <Provider store={store}>
        <MemoryRouter initialEntries={[route]}>
          <Routes>
            <Route path="/" element={<div>Home</div>} />
            <Route path="/login" element={<div>Login</div>} />
            <Route path="/protected" element={<ProtectedRoute allowedRoles={['Admin']} />}>
              <Route path="" element={<div>Protected Content</div>} />
            </Route>
          </Routes>
          {ui}
        </MemoryRouter>
      </Provider>
    );
  };

  test('redirects to login if not authenticated', () => {
    renderWithRouter(null, { route: '/protected' });

    expect(screen.getByText('Login')).toBeInTheDocument();
  });

  test('redirects to home if authenticated but role is not allowed', () => {
    store = mockStore({
      auth: {
        token: 'test-token',
        role: 'User',
      },
    });

    renderWithRouter(null, { route: '/protected' });

    expect(screen.getByText('Home')).toBeInTheDocument();
  });

  test('renders protected content if authenticated and role is allowed', () => {
    store = mockStore({
      auth: {
        token: 'test-token',
        role: 'Admin',
      },
    });

    renderWithRouter(null, { route: '/protected' });

    expect(screen.getByText('Protected Content')).toBeInTheDocument();
  });
});
