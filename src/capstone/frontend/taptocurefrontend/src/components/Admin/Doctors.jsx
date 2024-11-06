import React, { useState, useEffect } from "react";
import axios from "axios";
import "../../css/doctorsdisplay.css"; // Separate CSS
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

const Doctors = () => {
  const [doctors, setDoctors] = useState([]);
  const [filter, setFilter] = useState("Approved");
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);

  const token = useSelector((state) => state.auth.token);
  console.log(token);

  useEffect(() => {
    fetchDoctors();
  }, [filter]);

  const fetchDoctors = () => {
    axios
      .get(
        `http://localhost:8091/doctorsdisplay/doctors?approvalStatus=${filter}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      )
      .then((response) => setDoctors(response.data))
      .catch((error) => console.error("Error fetching doctors:", error));
  };

  const filteredDoctors = doctors.filter((doctor) =>
    doctor.medicalLicenseNumber.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const doctorsPerPage = 10;
  const pageCount = Math.ceil(filteredDoctors.length / doctorsPerPage);

  const handlePageClick = ({ selected }) => {
    setCurrentPage(selected);
  };

  const offset = currentPage * doctorsPerPage;
  const currentDoctors = filteredDoctors.slice(offset, offset + doctorsPerPage);

  return (
    <div>
      <Navbar />
      <div className="doctors-display">
        <div className="doctors-display__sidebar">
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
        <div className="doctors-display__content">
          <h1>Doctors List</h1>
          <div className="doctors-display__filter">
            <input
              type="text"
              placeholder="Search by License Number"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="doctors-display__search"
            />
            <label htmlFor="approval-filter">Filter by Approval Status: </label>
            <select id="approval-filter" value={filter} onChange={(e) => setFilter(e.target.value)}>
              <option value="Approved">Approved</option>
              <option value="Rejected">Rejected</option>
            </select>
          </div>
          <div className="doctors-display__table-container">
            <table className="doctors-display__table">
              <thead>
                <tr>
                  <th>Serial No.</th>
                  <th>Name</th>
                  <th>License Number</th>
                  <th>Email</th>
                  <th>Specialization</th>
                  <th>Years of Experience</th>
                  <th>Hospital</th>
                </tr>
              </thead>
              <tbody>
                {currentDoctors.map((doctor,index) => (
                  <tr key={doctor.doctorId}>
                    <td>{index+1+offset}</td>
                    <td>{doctor.name}</td>
                    <td>{doctor.medicalLicenseNumber}</td>
                    <td>{doctor.email}</td>
                    <td>{doctor.specialization}</td>
                    <td>{doctor.yearsOfExperience}</td>
                    <td>{doctor.hospitalName}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            <ReactPaginate
              previousLabel={'previous'}
              nextLabel={'next'}
              breakLabel={'...'}
              breakClassName={'doctors-display-break-me'}
              pageCount={pageCount}
              marginPagesDisplayed={2}
              pageRangeDisplayed={5}
              onPageChange={handlePageClick}
              containerClassName={'doctors-display-pagination'}
              activeClassName={'doctors-display-active'}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Doctors;
