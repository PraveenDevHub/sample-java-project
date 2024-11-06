import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useSelector } from 'react-redux';

const ProtectedRoute = ({ allowedRoles }) => {
    const token = useSelector((state) => state.auth.token);
    const role = useSelector((state) => state.auth.role);

    if (!token) {
        return <Navigate to="/login" />;
    }

    if (!allowedRoles.includes(role)) {
        return <Navigate to="/" />; // Redirect to a default page or an unauthorized page
    }

    return <Outlet />;
};

export default ProtectedRoute;
