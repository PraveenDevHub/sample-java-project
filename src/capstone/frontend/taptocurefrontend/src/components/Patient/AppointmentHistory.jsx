import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../../css/appointmenthistory.css';
import { useSelector } from 'react-redux';
import Navbar from '../Subcomponents/Navbar';
import Footer from '../Subcomponents/Footer';
import ReactPaginate from 'react-paginate';

const AppointmentHistory = () => {
    const [appointmentHistory, setAppointmentHistory] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const navigate = useNavigate();
    const [error, setError] = useState(null);

    const patientId = useSelector(state => state.auth.patientId);
    const token = useSelector((state) => state.auth.token);

    useEffect(() => {
        if (!patientId) {
            setError('Doctor ID not found. Please log in again.');
            navigate('/login');
        } else {
            const fetchAppointmentHistory = async () => {
                try {
                    const response = await axios.get(`http://localhost:8091/appointment/history/${patientId}`, {
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    });
                    setAppointmentHistory(response.data);
                } catch (error) {
                    console.error('Error fetching patient appointment history', error);
                }
            };

            fetchAppointmentHistory();
        }
    }, [patientId]);

    const appointmentsPerPage = 10;
    const pageCount = Math.ceil(appointmentHistory.length / appointmentsPerPage);

    const handlePageClick = ({ selected }) => {
        setCurrentPage(selected);
    };

    const offset = currentPage * appointmentsPerPage;
    const currentAppointments = appointmentHistory.slice(offset, offset + appointmentsPerPage);

    return (
        <div className="appointment-history-container">
            <Navbar />
            <div className="appointment-history-div">
                <h3>Appointment History</h3>
                {appointmentHistory.length > 0 ? (
                    <>
                        <table className="appointment-history-table">
                            <thead>
                                <tr>
                                    <th>serial No.</th>
                                    <th>Doctor Name</th>
                                    <th>Date</th>
                                    <th>Time</th>
                                    <th>Reason</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                {currentAppointments.map((appointment,index) => (
                                    <tr key={appointment.appointmentId}>
                                        <td>{index+1+offset}</td>
                                        <td data-label="Doctor Name">{appointment.doctor.user.name}</td>
                                        <td data-label="Date">{appointment.appointmentDate}</td>
                                        <td data-label="Time">{appointment.appointmentTimeSlot}</td>
                                        <td data-label="Reason">{appointment.reasonForVisit}</td>
                                        <td data-label="Status">{appointment.status}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                        <ReactPaginate
                            previousLabel={'previous'}
                            nextLabel={'next'}
                            breakLabel={'...'}
                            breakClassName={'appointment-history-break-me'}
                            pageCount={pageCount}
                            marginPagesDisplayed={2}
                            pageRangeDisplayed={5}
                            onPageChange={handlePageClick}
                            containerClassName={'appointment-history-pagination'}
                            activeClassName={'appointment-history-active'}
                        />
                    </>
                ) : (
                    <p>No appointment history available.</p>
                )}
            </div>
            <div className="doc-footer">
                <Footer />
            </div>
        </div>
    );
};

export default AppointmentHistory;
