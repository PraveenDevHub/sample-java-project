import React, { useEffect, useState } from "react";
import axios from "axios";
import "../../css/admindashboard.css";
import Navbar from "../Subcomponents/Navbar";
import {
  FaUserMd,
  FaClipboardCheck,
  FaChartBar,
  FaTachometerAlt,
  FaUser,
} from "react-icons/fa";
import { useSelector } from "react-redux"; // Assuming you're using Redux

const AdminDashboard = () => {
  const [counts, setCounts] = useState({
    patients: 0,
    doctors: 0,
    approvals: 0,
  });

  const token = useSelector((state) => state.auth.token);
  console.log(token);
  useEffect(() => {
    axios
      .get("http://localhost:8091/admin/dashboard-counts", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        setCounts(response.data);
      });
  }, []);

  return (
    <div>
      <Navbar />
      <div className="admin-dashboard">
        <div className="admin-dashboard__sidebar">
          <h2>Admin Panel</h2>
          <ul>
            <li onClick={() => (window.location.href = "/adminhome")}>
              <FaTachometerAlt /> Dashboard
            </li>
            <li onClick={() => (window.location.href = "/patients")}>
              <FaUser /> Patients
            </li>
            <li onClick={() => (window.location.href = "/doctors")}>
              <FaUserMd /> Doctors
            </li>
            <li onClick={() => (window.location.href = "/pending-approvals")}>
              <FaClipboardCheck /> Pending Approvals
            </li>
            <li onClick={() => (window.location.href = "/statistics")}>
              <FaChartBar /> Statistics
            </li>
          </ul>
        </div>
        <div className="admin-dashboard__content">
          <h1>Admin Dashboard</h1>
          <div className="admin-dashboard__options">
            <div
              className="admin-dashboard__option card"
              onClick={() => (window.location.href = "/patients")}
            >
              <FaUser className="admin-dashboard__icon" />
              <p>Total Patients</p>
              <span>{counts.patients}</span>
            </div>
            <div
              className="admin-dashboard__option card"
              onClick={() => (window.location.href = "/doctors")}
            >
              <FaUserMd className="admin-dashboard__icon" />
              <p>Total Doctors</p>
              <span>{counts.doctors}</span>
            </div>
            <div
              className="admin-dashboard__option card"
              onClick={() => (window.location.href = "/pending-approvals")}
            >
              <FaClipboardCheck className="admin-dashboard__icon" />
              <p>Pending Approvals</p>
              <span>{counts.approvals}</span>
            </div>
            <div
              className="admin-dashboard__option card"
              onClick={() => (window.location.href = "/statistics")}
            >
              <FaChartBar className="admin-dashboard__icon" />
              <p>View Statistics</p>
            </div>
          </div>
          {/* <div className="admin-dashboard__statistics">
          <h2>Statistics Overview</h2>
          <p>Total Patients: {counts.patients}</p>
          <p>Total Doctors: {counts.doctors}</p>
          <p>Pending Approvals: {counts.approvals}</p>
        </div> */}
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
