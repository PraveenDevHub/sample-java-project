import React, { useState, useEffect } from "react";
import axios from "axios";
import "../../css/patientdisplay.css"; // Separate CSS
import {
  FaUserMd,
  FaClipboardCheck,
  FaChartBar,
  FaTachometerAlt,
  FaUser,
} from "react-icons/fa";
import { useSelector } from "react-redux";
import Navbar from "../Subcomponents/Navbar";
import ReactPaginate from 'react-paginate';

const Patients = () => {
  const [patients, setPatients] = useState([]);
  const [searchEmail, setSearchEmail] = useState("");
  const [currentPage, setCurrentPage] = useState(0);

  const token = useSelector((state) => state.auth.token);
  console.log(token);

  useEffect(() => {
    fetchPatients();
  }, []);

  const fetchPatients = () => {
    axios
      .get("http://localhost:8091/patientsdisplay/patients", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => setPatients(response.data))
      .catch((error) => console.error("Error fetching patients:", error));
  };

  const filteredPatients = patients.filter((patient) =>
    patient.email.toLowerCase().includes(searchEmail.toLowerCase())
  );

  const patientsPerPage = 10;
  const pageCount = Math.ceil(filteredPatients.length / patientsPerPage);

  const handlePageClick = ({ selected }) => {
    setCurrentPage(selected);
  };

  const offset = currentPage * patientsPerPage;
  const currentPatients = filteredPatients.slice(offset, offset + patientsPerPage);

  return (
    <div>
      <Navbar />
      <div className="patients-display-container">
        <div className="patients-display-sidebar">
          <h2 className="patients-display-sidebar-title">Admin Panel</h2>
          <ul className="patients-display-sidebar-menu">
            <li
              className="patients-display-sidebar-menu-item"
              onClick={() => (window.location.href = "/adminhome")}
            >
              <FaTachometerAlt /> Dashboard
            </li>
            <li
              className="patients-display-sidebar-menu-item"
              onClick={() => (window.location.href = "/patients")}
            >
              <FaUser /> Patients
            </li>
            <li
              className="patients-display-sidebar-menu-item"
              onClick={() => (window.location.href = "/doctors")}
            >
              <FaUserMd /> Doctors
            </li>
            <li
              className="patients-display-sidebar-menu-item"
              onClick={() => (window.location.href = "/pending-approvals")}
            >
              <FaClipboardCheck /> Pending Approvals
            </li>
            <li
              className="patients-display-sidebar-menu-item"
              onClick={() => (window.location.href = "/statistics")}
            >
              <FaChartBar /> Statistics
            </li>
          </ul>
        </div>

        {/* Content */}
        <div className="patients-display-content">
          <h1 className="patients-display-title">Patients List</h1>

          {/* Search Filter */}
          <div className="patients-display-filter-container">
            <input
              type="text"
              className="patients-display-search-bar"
              placeholder="Search by email..."
              value={searchEmail}
              onChange={(e) => setSearchEmail(e.target.value)}
            />
          </div>

          {/* Table Container for Scrollability */}
          <div className="patients-display-table-container">
            <table className="patients-display-table">
              <thead>
                <tr>
                  <th className="patients-display-table-header">Serial No.</th>
                  <th className="patients-display-table-header">Name</th>
                  <th className="patients-display-table-header">Email</th>
                  <th className="patients-display-table-header">Age</th>
                  <th className="patients-display-table-header">Gender</th>
                </tr>
              </thead>
              <tbody>
                {currentPatients.map((patient,index) => (
                  <tr
                    key={patient.patientId}
                    className="patients-display-table-row"
                  >
                    <td className="patients-display-table-cell">
                      {index+1+offset}
                    </td>
                    <td className="patients-display-table-cell">
                      {patient.name}
                    </td>
                    <td className="patients-display-table-cell">
                      {patient.email}
                    </td>
                    <td className="patients-display-table-cell">
                      {patient.age}
                    </td>
                    <td className="patients-display-table-cell">
                      {patient.gender}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            <ReactPaginate
              previousLabel={'previous'}
              nextLabel={'next'}
              breakLabel={'...'}
              breakClassName={'patients-display-break-me'}
              pageCount={pageCount}
              marginPagesDisplayed={2}
              pageRangeDisplayed={5}
              onPageChange={handlePageClick}
              containerClassName={'patients-display-pagination'}
              activeClassName={'patients-display-active'}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Patients;
