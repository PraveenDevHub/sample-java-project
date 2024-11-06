import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../../css/doctordashboard.css";
import { useSelector } from "react-redux";
import Footer from "../Subcomponents/Footer";
import Navbar from "../Subcomponents/Navbar";
import Swal from 'sweetalert2';
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes } from "@fortawesome/free-solid-svg-icons";
import { FaCalendarAlt, FaClipboardCheck, FaClock, FaRegTimesCircle, FaStickyNote, FaUserMd } from "react-icons/fa";
import { LABELS, MESSAGES, ROUTES, BUTTON_TEXT, TITLES } from './constants';

const PatientAppointments = () => {
  const navigate = useNavigate();
  const [appointments, setAppointments] = useState([]);
  const [filterDate, setFilterDate] = useState("");
  const [error, setError] = useState(null);
  const [openCancelDialog, setOpenCancelDialog] = useState(false);
  const [selectedAppointmentId, setSelectedAppointmentId] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const patientId = useSelector((state) => state.auth.patientId);
  const token = useSelector((state) => state.auth.token);

  console.log("patient id fetched: ", patientId);

  // Fetch all appointments on page load
  useEffect(() => {
    if (!patientId) {
      setError(MESSAGES.PATIENT_ID_NOT_FOUND);
      navigate("/login");
    } else {
      fetchAppointments();
    }
  }, [patientId]);

  const fetchAppointments = async (date = "") => {
    try {
      const url = `${process.env.REACT_APP_API_BASE_URL}${ROUTES.UPCOMING_APPOINTMENTS(patientId)}`;

      const response = await axios.get(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (date) {
        const filtered = response.data.filter(
          (app) =>
            new Date(app.appointmentDate).toISOString().slice(0, 10) === date
        );
        setAppointments(filtered);
      } else {
        setAppointments(response.data);
        setError("");
      }
    } catch (error) {
      setError(MESSAGES.FETCH_APPOINTMENTS_ERROR);
      toast.error(MESSAGES.FETCH_APPOINTMENTS_ERROR, {
        position: "bottom-left",
        autoClose: 5000,
      });
    }
  };

  const cancelAppointment = async (appointmentId) => {
    const confirmCancel = await Swal.fire({
      title: TITLES.ARE_YOU_SURE,
      text: MESSAGES.CANCEL_APPOINTMENT_CONFIRM,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: BUTTON_TEXT.YES_CANCEL_IT,
      cancelButtonText: BUTTON_TEXT.NO_KEEP_IT
    });

    if (confirmCancel.isConfirmed) {
      // Show loader
      Swal.fire({
        title: TITLES.PLEASE_WAIT,
        text: 'Cancelling appointment',
        allowOutsideClick: false,
        didOpen: () => {
          Swal.showLoading();
        }
      });

      try {
        await axios.put(`${process.env.REACT_APP_API_BASE_URL}${ROUTES.CANCEL_APPOINTMENT(appointmentId, patientId)}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });

        // Close loader and show success message
        Swal.fire({
          icon: 'success',
          title: TITLES.CANCELLED,
          text: MESSAGES.CANCEL_APPOINTMENT_SUCCESS,
          timer: 3000,
          showConfirmButton: false
        });

        fetchAppointments();
      } catch (error) {
        console.error('Error cancelling appointment:', error);

        // Close loader and show error message
        Swal.fire({
          icon: 'error',
          title: TITLES.ERROR,
          text: MESSAGES.CANCEL_APPOINTMENT_ERROR,
          timer: 3000,
          showConfirmButton: false
        });
      }
    }
  };

  // Handle date filter change
  const handleDateFilter = (e) => {
    const selectedDate = e.target.value;
    setFilterDate(selectedDate);
    fetchAppointments(selectedDate);
  };

  // Display all appointments
  const handleDisplayAll = () => {
    setFilterDate("");
    fetchAppointments();
  };

  return (
    <div className="doctordashboard-container">
      <header>
        <Navbar />
      </header>
      <div className="content">
      <h2>Upcoming Appointments</h2>
        <div className="filter-container">
          <label htmlFor="filterDate">{LABELS.FILTER_BY_DATE}</label>
          <input
            type="date"
            id="filterDate"
            value={filterDate}
            onChange={handleDateFilter}
          />
          <button className="displayall-button" onClick={handleDisplayAll}>
            {BUTTON_TEXT.DISPLAY_ALL}
          </button>
        </div>

        <div className="appointment-cards">
          {error && <p className="error-message">{error}</p>}
          {appointments.length > 0 ? (
            appointments.map((appointment) => (
              <div key={appointment.appointmentId} className="appointment-card">
                <h4><FaUserMd /> Dr. {appointment.doctor.user.name}</h4>
                <p><FaCalendarAlt /> {LABELS.DATE} {new Date(appointment.appointmentDate).toLocaleDateString()}</p>
                <p><FaClock /> {LABELS.TIME} {appointment.appointmentTimeSlot}</p>
                <p><FaStickyNote /> {LABELS.REASON} {appointment.reasonForVisit}</p>
                <p><FaClipboardCheck /> {LABELS.STATUS} {appointment.status}</p>
                {appointment.status === 'Scheduled' && (
                  <div>
                    <button className="cancel-button" onClick={() => cancelAppointment(appointment.appointmentId)}>
                      <FaRegTimesCircle style={{ marginRight: '8px' }} />
                      {LABELS.CANCEL}
                    </button>
                  </div>
                )}
              </div>
            ))
          ) : (
            <p>{LABELS.NO_APPOINTMENTS}</p>
          )}
        </div>
      </div>
      <div className='doc-footer'>
        <Footer />
      </div>
      <ToastContainer />
    </div>

  );
};

export default PatientAppointments;
