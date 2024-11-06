import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../../css/BookAppointment.css';
import { useSelector } from 'react-redux';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser, faEnvelope, faIdCard, faStethoscope, faCalendarAlt, faHospital } from '@fortawesome/free-solid-svg-icons';
import { useLocation, useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
import Navbar from '../Subcomponents/Navbar';
import Footer from '../Subcomponents/Footer';
import { FaAudioDescription, FaFileAlt, FaFileContract, FaLandmark } from 'react-icons/fa';
 
import { LABELS, MESSAGES, ROUTES, BUTTON_TEXT, TITLES } from './constants';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const BookAppointment = () => {
    const token = useSelector((state) => state.auth.token);
    const patientId = useSelector((state) => state.auth.patientId);
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const doctorId = queryParams.get('doctorId');
    const [availableDates, setAvailableDates] = useState([]);
    const [selectedDate, setSelectedDate] = useState('');
    const [availableTimeSlots, setAvailableTimeSlots] = useState([]);
    const [selectedTime, setSelectedTime] = useState('');
    const [reasonForVisit, setReasonForVisit] = useState('');
    const [loadingDates, setLoadingDates] = useState(false);
    const [loadingSlots, setLoadingSlots] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(false);
    const navigate = useNavigate();
    const [doctorData, setDoctorData] = useState({
        name: '',
        specialization: '',
        yearsOfExperience: '',
        doctorDescription: '',
        profilePhoto: null,
        hospitalName: '',
        state: '',
        city: '',
    });

    useEffect(() => {
      const fetchDoctorProfile = async () => {
        setLoading(true);

        try {
          let url = `http://localhost:8091/doctorProfile/${doctorId}`;

          const response = await axios.get(url, {
            headers: {
              Authorization: `Bearer ${token}`, // Add the token in the Authorization header
            },
          });

          const doctor = response.data;

          setDoctorData((prevData) => ({
            ...prevData,

            name: doctor.user.name,

            specialization: doctor.specialization,

            yearsOfExperience: doctor.yearsOfExperience,

            doctorDescription: doctor.doctorDescription,

            profilePhoto: doctor.profilePhoto,

            hospitalName: doctor.hospitalName,

            state: doctor.state,

            city: doctor.city,
          }));
        } catch (error) {
          if (error.response && error.response.status === 404) {
            console.error("Doctor profile not found:", error);

            setError(
              "Doctor profile not found. Please check the doctor ID and try again."
            );
          } else {
            console.error("Error fetching the doctor profile:", error);

            setError(
              error.response?.data?.message ||
                "Error fetching the doctor profile"
            );
          }
        } finally {
          setLoading(false);
        }
      };

      fetchDoctorProfile();
    }, [doctorId]);

 
 
    useEffect(() => {
      const fetchAvailableDates = async () => {
        setLoadingDates(true);

        try {
          const response = await axios.get(
            `http://localhost:8091/patient/appointment/available-dates?doctorId=${doctorId}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );

          const today = new Date().toISOString().split("T")[0];

          setAvailableDates(
            response.data
              .map((date) => new Date(date).toISOString().split("T")[0])
              .filter((date) => date >= today)
          );
        } catch (error) {
          if (error.response && error.response.status === 404) {
            console.error("No slots available for the selected date:", error);

            // toast.error('No slots available for the selected date. Please choose another date.');

            toast.error(error.response.data);
          } else {
            console.error("Error fetching available dates:", error);

            toast.error(
              "Failed to fetch available dates. Please try again later."
            );
          }
        } finally {
          setLoadingDates(false);
        }
      };

      fetchAvailableDates();
    }, [doctorId, token]);
        
 
    const handleDateChange = (event) => {
      const date = event.target.value;

      setSelectedDate(date);

      setSelectedTime("");

      setLoadingSlots(true);

      const fetchAvailableTimeSlots = async (selectedDate) => {
        try {
          const response = await axios.get(
            `http://localhost:8091/patient/appointment/available-slots?doctorId=${doctorId}&date=${selectedDate}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );

          const currentTime = new Date()
            .toISOString()
            .split("T")[1]
            .split(":")
            .slice(0, 2)
            .join(":");

          const filteredSlots = response.data.filter((slot) => {
            if (selectedDate === new Date().toISOString().split("T")[0]) {
              return slot >= currentTime;
            }

            return true;
          });

          setAvailableTimeSlots(filteredSlots);
        } catch (error) {
          if (error.response && error.response.status === 404) {
            console.error("No slots available for the selected date:", error);

            // toast.error('No slots available for the selected date. Please choose another date.');

            toast.error(error.response.data);
          } else {
            console.error("Error fetching available time slots:", error);

            toast.error(
              "Failed to fetch available time slots. Please try again later."
            );
          }
        } finally {
          setLoadingSlots(false);
        }
      };

      fetchAvailableTimeSlots(date);
    };
 

    const handleTimeChange = (time) => {
        setSelectedTime(time);
    };

    const handleReasonChange = (event) => {
        setReasonForVisit(event.target.value);
    };

    const handleSubmit = async () => {
        if (selectedDate && selectedTime && reasonForVisit) {
            Swal.fire({
                title: TITLES.CONFIRM_APPOINTMENT,
                text: MESSAGES.CONFIRM_APPOINTMENT(new Date(selectedDate).toLocaleDateString(), selectedTime),
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: BUTTON_TEXT.YES_BOOK_IT,
                cancelButtonText: BUTTON_TEXT.CANCEL,
            }).then(async (result) => {
                if (result.isConfirmed) {
                    Swal.fire({
                        title: TITLES.BOOKING_APPOINTMENT,
                        text: MESSAGES.BOOKING_APPOINTMENT,
                        allowOutsideClick: false,
                        didOpen: () => {
                            Swal.showLoading();
                        }
                    });

                    try {
                        await axios.post(`http://localhost:8091/patient/appointment/bookappointment`, {
                            doctor: { doctorId },
                            patient: { patientId },
                            appointmentDate: selectedDate,
                            appointmentTimeSlot: selectedTime,
                            reasonForVisit,
                            status: 'Scheduled'
                        }, {
                            headers: {
                                Authorization: `Bearer ${token}`,
                            }
                        });

                        Swal.fire({
                            title: TITLES.APPOINTMENT_BOOKED,
                            text: MESSAGES.APPOINTMENT_BOOKED(new Date(selectedDate).toLocaleDateString(), selectedTime),
                            icon: 'success',
                            confirmButtonText: BUTTON_TEXT.OK
                        });

                        navigate(ROUTES.PATIENT_APPOINTMENTS);
                        setAvailableTimeSlots(prevSlots => prevSlots.filter(slot => slot !== selectedTime));

                    } catch (error) {
                        Swal.fire({
                            title: TITLES.BOOKING_FAILED,
                            text: MESSAGES.BOOKING_FAILED,
                            icon: 'error',
                            confirmButtonText: BUTTON_TEXT.OK
                        });
                        navigate(ROUTES.PATIENT_HOME);
                    }
                }
            });
        } else {
            Swal.fire({
                title: TITLES.INCOMPLETE_INFORMATION,
                text: LABELS.FILL_ALL_FIELDS,
                icon: 'warning',
                confirmButtonText: BUTTON_TEXT.OK
            });
        }
    };

    return (
        <div className='book-appointment-container'>
            <Navbar />
            <div className="book-appointment">
                {loading && <p>{LABELS.LOADING}</p>}
                <div className="book-appointment__container">
                    <div className="book-appointment__doctor-details">
                        <h2>{LABELS.DOCTOR_DETAILS}</h2>
                        <p style={{ fontSize: "19px", fontWeight: "bold" }}>
                            {doctorData.profilePhoto && (
                                <img src={doctorData.profilePhoto} alt="Doctor Profile" className="doctor-profile-photo" />
                            )} Dr.{doctorData.name}
                        </p>
                        <p><FontAwesomeIcon icon={faStethoscope} /><strong> {LABELS.SPECIALIZATION}</strong> {doctorData.specialization}</p>
                        <p><FontAwesomeIcon icon={faCalendarAlt} /><strong> {LABELS.EXPERIENCE}</strong> {doctorData.yearsOfExperience} years</p>
                        <p><FontAwesomeIcon icon={faHospital} /><strong> {LABELS.HOSPITAL}</strong> {doctorData.hospitalName}</p>
                        <p><FaLandmark /> <strong> {LABELS.LOCATION}</strong> {doctorData.city}, {doctorData.state}</p>
                        <p><FaFileContract /><strong> {LABELS.DESCRIPTION}</strong> {doctorData.doctorDescription}</p>
                    </div>

                    <div className="book-appointment__form">
                        <h2 className="book-appointment__title">{LABELS.BOOK_APPOINTMENT}</h2>

                        <div className="book-appointment__dates">
                            <label htmlFor="date">{LABELS.SELECT_DATE}</label>
                            {loadingDates ? (
                                <p>{LABELS.LOADING_DATES}</p>
                            ) : (
                                <input
                                    type="date"
                                    id="date"
                                    value={selectedDate}
                                    onChange={handleDateChange}
                                    min={availableDates[0]}
                                    max={availableDates[availableDates.length - 1]}
                                />
                            )}
                        </div>

                        {selectedDate && (
                            <div className="book-appointment__slots">
                                <label>{LABELS.SELECT_TIME_SLOT}</label>
                                {loadingSlots ? (
                                    <p>{LABELS.LOADING_SLOTS}</p>
                                ) : (
                                    <div className="book-appointment__time-slot-grid">
                                        {availableTimeSlots.length > 0 ? (
                                            availableTimeSlots.map((slot, index) => (
                                                <button
                                                    key={index}
                                                    type="button"
                                                    className={`book-appointment__time-slot-btn ${selectedTime === slot ? 'selected' : ''}`}
                                                    onClick={() => handleTimeChange(slot)}
                                                    disabled={selectedTime === slot}
                                                >
                                                    {slot}
                                                </button>
                                            ))
                                        ) : (
                                            <p>{LABELS.NO_AVAILABLE_SLOTS}</p>
                                        )}
                                    </div>
                                )}
                            </div>
                        )}

                        {selectedTime && (
                            <div className="book-appointment__reason">
                                <label htmlFor="reason">{LABELS.REASON_FOR_VISIT}</label>
                                <input
                                    type="text"
                                    id="reason"
                                    value={reasonForVisit}
                                    onChange={handleReasonChange}
                                    placeholder="Enter reason for visit"
                                />
                            </div>
                        )}

                        {selectedTime && reasonForVisit && (
                            <div className="book-appointment__submit">
                                <button className="book-appointment__btn-primary" onClick={handleSubmit}>
                                    {LABELS.CONFIRM_APPOINTMENT}
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            </div>
            <Footer />
        </div>
    );
};

export default BookAppointment;

