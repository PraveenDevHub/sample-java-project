import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../../css/doctordashboard.css";
import { useSelector } from "react-redux";
import Footer from "../Subcomponents/Footer";
import Navbar from "../Subcomponents/Navbar";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Swal from 'sweetalert2';
import { FaClipboardCheck, FaRegCalendarAlt, FaRegCheckCircle, FaRegClock, FaRegStickyNote, FaRegTimesCircle, FaUserMd } from 'react-icons/fa';
import {
  GET_APPOINTMENTS_URL,
  CANCEL_APPOINTMENT_URL,
  COMPLETE_APPOINTMENT_URL,
  CANCEL_ALL_APPOINTMENTS_URL,
  TOAST_OPTIONS,
  DIALOG_MESSAGES,
  BUTTON_LABELS,
  FORM_LABELS,
  TOAST_MESSAGES,
  ERROR_MESSAGES,
  TITLES,
  TEXTS,
  LOG_MESSAGES,
  APPOINTMENT_LABELS,
} from "./constants";

const DoctorHome = () => {
  const navigate = useNavigate();
  const [appointments, setAppointments] = useState([]);
  const [filterDate, setFilterDate] = useState("");
  const [error, setError] = useState(null);
  const [showCancelAllPopup, setShowCancelAllPopup] = useState(false);
  const [cancelAllDate, setCancelAllDate] = useState('');
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);

  const doctorId = useSelector((state) => state.auth.doctorId);
  const token = useSelector((state) => state.auth.token);

  console.log(LOG_MESSAGES.DOCTOR_ID_FETCHED, doctorId); // Adjust this path to match your store structure

  useEffect(() => {
    if (!doctorId) {
      setError(ERROR_MESSAGES.DOCTOR_ID_NOT_FOUND);
      navigate("/login"); // Redirect to login if doctor ID is missing
    } else {
      fetchAppointments(); // Fetch appointments when doctorId is present
    }
  }, [doctorId]);

  const fetchAppointments = async (date = "") => {
    try {
      const url = GET_APPOINTMENTS_URL(doctorId);

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
        setError(""); // Display all appointments
      }
    } catch (error) {
      setError(TOAST_MESSAGES.FETCH_APPOINTMENTS_FAILURE);
      toast.error(TOAST_MESSAGES.FETCH_APPOINTMENTS_FAILURE, TOAST_OPTIONS);
    }
  };

  const cancelAppointment = async (appointmentId) => {
    const confirmCancel = await Swal.fire({
      title: TITLES.CONFIRM_ACTION,
      text: DIALOG_MESSAGES.CANCEL_APPOINTMENT,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: BUTTON_LABELS.YES_CANCEL_ALL,
      cancelButtonText: BUTTON_LABELS.NO_GO_BACK
    });

    if (confirmCancel.isConfirmed) {
      Swal.fire({
        title: TEXTS.CANCEL_APPOINTMENT,
        text: TEXTS.CANCEL_ALL_APPOINTMENTS,
        allowOutsideClick: false,
        didOpen: () => {
          Swal.showLoading();
        }
      });

      try {
        await axios.put(CANCEL_APPOINTMENT_URL(appointmentId),{}, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        Swal.fire({
          icon: 'success',
          title: TITLES.CANCELLED,
          text: TOAST_MESSAGES.CANCEL_APPOINTMENT_SUCCESS,
          timer: 3000,
          showConfirmButton: false
        });

        fetchAppointments(); // Refresh the appointments list
      } catch (error) {
        console.error(LOG_MESSAGES.ERROR_CANCELLING_APPOINTMENT, error);

        Swal.fire({
          icon: 'error',
          title: TITLES.ERROR,
          text: TOAST_MESSAGES.CANCEL_APPOINTMENT_FAILURE,
          timer: 3000,
          showConfirmButton: false
        });
      }
    }
  };

  const handleCompleteAppointment = async (appointmentId) => {
    const confirmComplete = await Swal.fire({
      title: TITLES.CONFIRM_ACTION,
      text: DIALOG_MESSAGES.COMPLETE_APPOINTMENT,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: BUTTON_LABELS.YES_CANCEL_ALL,
      cancelButtonText: BUTTON_LABELS.NO_GO_BACK
    });

    if (confirmComplete.isConfirmed) {
      Swal.fire({
        title: TEXTS.COMPLETE_APPOINTMENT,
        text: "complete",
        allowOutsideClick: false,
        didOpen: () => {
          Swal.showLoading();
        }
      });

      try {
        await axios.put(COMPLETE_APPOINTMENT_URL(appointmentId),{},{
          headers: {
            Authorization: `Bearer ${token}`
          }
        });

        Swal.fire({
          icon: 'success',
          title: TITLES.COMPLETED,
          text: TOAST_MESSAGES.COMPLETE_APPOINTMENT_SUCCESS,
          timer: 3000,
          showConfirmButton: false
        });

        fetchAppointments(); // Refresh the appointments list
      } catch (error) {
        console.error(LOG_MESSAGES.ERROR_COMPLETING_APPOINTMENT, error);

        Swal.fire({
          icon: 'error',
          title: TITLES.ERROR,
          text: TOAST_MESSAGES.COMPLETE_APPOINTMENT_FAILURE,
          timer: 3000,
          showConfirmButton: false
        });
      }
    }
  };

  const handleDateFilter = (e) => {
    const selectedDate = e.target.value;
    setFilterDate(selectedDate);
    fetchAppointments(selectedDate); // Fetch appointments filtered by selected date
  };

  const handleDisplayAll = () => {
    setFilterDate("");
    fetchAppointments(); // Fetch all appointments without date filtering
  };

  const handleCancelAllClick = () => {
    setShowCancelAllPopup(true);
  };

  const handleCancelAllDateChange = (e) => {
    setCancelAllDate(e.target.value);
  };

  const handleConfirmCancelAll = () => {
    setShowConfirmDialog(true);
  };

  const handleCancelAllAppointments = async () => {
    if (cancelAllDate) {
      Swal.fire({
        title: TITLES.CANCEL_APPOINTMENTS,
        text: TEXTS.CANCEL_ALL_APPOINTMENTS,
        allowOutsideClick: false,
        didOpen: () => {
          Swal.showLoading();
        }
      });

      try {
        await axios.put(CANCEL_ALL_APPOINTMENTS_URL(doctorId), { date: cancelAllDate }, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });
        Swal.fire({
          icon: 'success',
          title: TITLES.CANCELLED,
          text: TOAST_MESSAGES.CANCEL_ALL_APPOINTMENTS_SUCCESS
        });

        setShowCancelAllPopup(false);
        setShowConfirmDialog(false);
        fetchAppointments(); // Refresh the appointments list
      } catch (error) {
        console.error(LOG_MESSAGES.ERROR_CANCELLING_APPOINTMENT, error);
        Swal.fire({
          icon: 'error',
          title: TITLES.ERROR,
          text: TOAST_MESSAGES.CANCEL_ALL_APPOINTMENTS_FAILURE
        });
      }
    } else {
      Swal.fire({
        icon: 'warning',
        title: TITLES.WARNING,
        text: DIALOG_MESSAGES.SELECT_DATE_WARNING
      });
    }
  };

  return (
    <div className="doctordashboard-container">
      <header>
        <Navbar />
      </header>
      <div className="content">
        <h2>Upcoming Appointments</h2>
        <div className="filter-container">
          <label htmlFor="filterDate">{FORM_LABELS.FILTER_BY_DATE}</label>
          <input
            type="date"
            id="filterDate"
            value={filterDate}
            onChange={handleDateFilter}
          />
          <button className="displayall-button" onClick={handleDisplayAll}>{BUTTON_LABELS.DISPLAY_ALL}</button>
          <button className="doctor-home-cancelall-button" onClick={handleCancelAllClick}>{BUTTON_LABELS.CANCEL_BY_DATE}</button>
        </div>

        {showCancelAllPopup && (
          <div className="doctor-home-cancelall-popup">
            <label htmlFor="cancelAllDate">{FORM_LABELS.SELECT_DATE_TO_CANCEL}</label>
            <input
              type="date"
              id="cancelAllDate"
              value={cancelAllDate}
              onChange={handleCancelAllDateChange}
            />
            <button className="doctor-home-confirm-button" onClick={handleConfirmCancelAll}>{BUTTON_LABELS.CONFIRM}</button>
            <button className="doctor-home-close-popup-button" onClick={() => setShowCancelAllPopup(false)}>{BUTTON_LABELS.CLOSE}</button>
          </div>
        )}

        {showConfirmDialog && (
          <div className="doctor-home-confirm-dialog">
            <p>{TEXTS.CANCEL_ALL_CONFIRM} {cancelAllDate}?</p>
            <button className="doctor-home-cancel-button" onClick={handleCancelAllAppointments}>{BUTTON_LABELS.YES_CANCEL_ALL}</button>
            <button className="doctor-home-close-popup-button" onClick={() => setShowConfirmDialog(false)}>{BUTTON_LABELS.NO_GO_BACK}</button>
          </div>
        )}

        <div className="appointment-cards">
          {error && <p className="error-message">{error}</p>}
          {appointments.length > 0 ? (
            appointments.map((appointment) => (
              <div key={appointment.appointmentId} className="appointment-card">
                <h4><FaUserMd /> {APPOINTMENT_LABELS.PATIENT}: {appointment.patient.user.name}</h4>
                <p><FaRegCalendarAlt /> {APPOINTMENT_LABELS.DATE}: {new Date(appointment.appointmentDate).toLocaleDateString()}</p>
                <p><FaRegClock /> {APPOINTMENT_LABELS.TIME}: {appointment.appointmentTimeSlot}</p>
                <p><FaRegStickyNote /> {APPOINTMENT_LABELS.REASON}: {appointment.reasonForVisit}</p>
                <p><FaClipboardCheck /> {APPOINTMENT_LABELS.STATUS}: {appointment.status}</p>

                {appointment.status === 'Scheduled' && (
                  <div className='appointment-cards-buttons'>
                    <button className="cancel-button" onClick={() => cancelAppointment(appointment.appointmentId)}>
                      <FaRegTimesCircle style={{ marginRight: '3px' }} />{BUTTON_LABELS.CANCEL}
                    </button>
                    <button className="complete-button" onClick={() => handleCompleteAppointment(appointment.appointmentId)}>
                      <FaRegCheckCircle style={{ marginRight: '3px' }} />{BUTTON_LABELS.COMPLETE}
                    </button>
                  </div>
                )}
              </div>
            ))
          ) : (
            <p>{ERROR_MESSAGES.NO_APPOINTMENTS}</p>
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

export default DoctorHome;
