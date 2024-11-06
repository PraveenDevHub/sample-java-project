import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useSelector } from "react-redux";

const PublicRoute = () => {
  const token = useSelector((state) => state.auth.token);
  const role = useSelector((state) => state.auth.role);

  if (token) {
    // Redirect based on role
    switch (role) {
      case "Doctor":
        return <Navigate to="/doctorhome" />;
      case "Patient":
        return <Navigate to="/patienthome" />;
      case "Admin":
        return <Navigate to="/adminhome" />;
      default:
        return <Navigate to="/" />;
    }
  }

  return <Outlet />;
};

export default PublicRoute;
