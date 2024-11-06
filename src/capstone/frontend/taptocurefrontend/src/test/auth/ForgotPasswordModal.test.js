import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import ForgotPasswordModal from '../../components/Auth/ForgotPasswordModal';
import axios from '../../axiosConfig';

jest.mock('../../axiosConfig');

jest.setTimeout(7000); // Increase the timeout to 7000 milliseconds for the entire test suite


describe('ForgotPasswordModal Component', () => {
    test('renders without crashing', () => {
        render(<ForgotPasswordModal showModal={true} closeModal={jest.fn()} />);
    });

    test('renders initial state correctly', () => {
        render(<ForgotPasswordModal showModal={true} closeModal={jest.fn()} />);
        expect(screen.getByPlaceholderText('Email')).toBeInTheDocument();
        expect(screen.getByText('Send Reset Link')).toBeInTheDocument();
    });

    test('submits form and shows success toast', async () => {
        axios.post.mockResolvedValue({ data: 'Password reset email sent successfully!' });

        render(<ForgotPasswordModal showModal={true} closeModal={jest.fn()} />);

        fireEvent.change(screen.getByPlaceholderText('Email'), { target: { value: 'test@example.com' } });
        fireEvent.click(screen.getByText('Send Reset Link'));

        await waitFor(() => {
            expect(screen.getByText('Password reset email sent successfully!')).toBeInTheDocument();
        });
    });

    test('submits form and shows error toast', async () => {
        axios.post.mockRejectedValue(new Error('Error sending password reset email'));

        render(<ForgotPasswordModal showModal={true} closeModal={jest.fn()} />);

        fireEvent.change(screen.getByPlaceholderText('Email'), { target: { value: 'test@example.com' } });
        fireEvent.click(screen.getByText('Send Reset Link'));

        await waitFor(() => {
            expect(screen.getByText('Error sending password reset email')).toBeInTheDocument();
        });
    });

    test('toast message disappears after 5 seconds', async () => {
        // jest.setTimeout(7000); // Increase the timeout to 7000 milliseconds for the entire test suite

        axios.post.mockResolvedValue({ data: 'Password reset email sent successfully!' });

        render(<ForgotPasswordModal showModal={true} closeModal={jest.fn()} />);

        fireEvent.change(screen.getByPlaceholderText('Email'), { target: { value: 'test@example.com' } });
        fireEvent.click(screen.getByText('Send Reset Link'));

        await waitFor(() => {
            expect(screen.getByText('Password reset email sent successfully!')).toBeInTheDocument();
        });

        await waitFor(() => {
            expect(screen.queryByText('Password reset email sent successfully!')).not.toBeInTheDocument();
        }, { timeout: 6000 });
    });

    test('handles loading state correctly', async () => {
        axios.post.mockResolvedValue({ data: 'Password reset email sent successfully!' });

        render(<ForgotPasswordModal showModal={true} closeModal={jest.fn()} />);

        fireEvent.change(screen.getByPlaceholderText('Email'), { target: { value: 'test@example.com' } });
        fireEvent.click(screen.getByText('Send Reset Link'));

        expect(screen.getByText('Sending...')).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.getByText('Send Reset Link')).toBeInTheDocument();
        });
    });

    test('handles modal visibility correctly', () => {
        const { rerender } = render(<ForgotPasswordModal showModal={false} closeModal={jest.fn()} />);
        expect(screen.queryByText('Forgot Password')).not.toBeInTheDocument();

        rerender(<ForgotPasswordModal showModal={true} closeModal={jest.fn()} />);
        expect(screen.getByText('Forgot Password')).toBeInTheDocument();
    });
});
