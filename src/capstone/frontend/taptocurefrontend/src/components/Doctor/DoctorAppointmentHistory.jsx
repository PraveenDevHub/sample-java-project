import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../../css/appointmenthistory.css';
import { useSelector } from 'react-redux';
import Navbar from '../Subcomponents/Navbar';
import Footer from '../Subcomponents/Footer';
import ReactPaginate from 'react-paginate';

const DoctorAppointmentHistory = () => {
    const [appointmentHistory, setAppointmentHistory] = useState([]);
    const [filteredAppointments, setFilteredAppointments] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedDate, setSelectedDate] = useState('');
    const [currentPage, setCurrentPage] = useState(0);
    const navigate = useNavigate();
    const [error, setError] = useState(null);

    const doctorId = useSelector(state => state.auth.doctorId);
    const token = useSelector((state) => state.auth.token);

    useEffect(() => {
        if (!doctorId) {
            setError('Doctor ID not found. Please log in again.');
            navigate('/login');
        } else {
            const fetchAppointmentHistory = async () => {
                try {
                    const response = await axios.get(`http://localhost:8091/appointment/doctors/history/${doctorId}`, {
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
    }, [doctorId]);

    useEffect(() => {
        let filtered = appointmentHistory;

        if (selectedDate) {
            filtered = filtered.filter(appointment => appointment.appointmentDate === selectedDate);
        }

        if (searchTerm) {
            filtered = filtered.filter(appointment =>
                appointment.patient.user.name.toLowerCase().includes(searchTerm.toLowerCase())
            );
        }

        setFilteredAppointments(filtered);
    }, [selectedDate, searchTerm, appointmentHistory]);

    const handleDisplayAll = () => {
        setSelectedDate('');
        setSearchTerm('');
        setFilteredAppointments(appointmentHistory);
    };

    const appointmentsPerPage = 10;
    const pageCount = Math.ceil(filteredAppointments.length / appointmentsPerPage);

    const handlePageClick = ({ selected }) => {
        setCurrentPage(selected);
    };

    const offset = currentPage * appointmentsPerPage;
    const currentAppointments = filteredAppointments.slice(offset, offset + appointmentsPerPage);

    return (
        <div className="appointment-history-container">
            <Navbar />
            <div className="appointment-history-div">
                <h3>Appointment History</h3>
                <div className="doctor-appointment-history-filters">
                    <input
                        type="date"
                        value={selectedDate}
                        onChange={(e) => setSelectedDate(e.target.value)}
                        className="doctor-appointment-history-date-filter"
                    />
                    <button onClick={handleDisplayAll} className="doctor-appointment-history-display-all">
                        Display All
                    </button>
                    <input
                        type="text"
                        placeholder="Search by patient name"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="doctor-appointment-history-search-bar"
                    />
                </div>
                {currentAppointments.length > 0 ? (
                    <>
                        <table className="appointment-history-table">
                            <thead>
                                <tr>
                                    <th>Serial No.</th>
                                    <th>Patient Name</th>
                                    <th>Date</th>
                                    <th>Time</th>
                                    <th>Reason</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                {currentAppointments.map((appointment, index) => (
                                    <tr key={appointment.appointmentId}>
                                        <td>{index + 1 + offset}</td>
                                        <td>{appointment.patient.user.name}</td>
                                        <td>{appointment.appointmentDate}</td>
                                        <td>{appointment.appointmentTimeSlot}</td>
                                        <td>{appointment.reasonForVisit}</td>
                                        <td>{appointment.status}</td>
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

export default DoctorAppointmentHistory;
