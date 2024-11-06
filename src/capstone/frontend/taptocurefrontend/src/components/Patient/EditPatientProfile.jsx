import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser, faCalendarAlt, faVenusMars, faPhone, faSave, faTimes, faEdit } from '@fortawesome/free-solid-svg-icons';
import '../../css/EditPatientProfile.css';
import { useSelector } from 'react-redux';
import Footer from '../Subcomponents/Footer';
import Navbar from '../Subcomponents/Navbar';
import Noty from 'noty';
import 'noty/lib/noty.css';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { LABELS, MESSAGES, ROUTES, BUTTON_TEXT, TITLES, GENDER_OPTIONS } from './constants';

const EditPatientProfile = () => {
    const token = useSelector((state) => state.auth.token);
    const patientId = useSelector((state) => state.auth.patientId);
    const [isEditing, setIsEditing] = useState(false);

    const handleEditClick = () => {
        setIsEditing(true);
    };

    const handleCancelClick = () => {
        setIsEditing(false);
    }
    const [patient, setPatient] = useState({
        name: '',
        age: '',
        gender: '',
        contact_number: ''
    });

    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchPatientProfile = async () => {
            setLoading(true);
            try {
                const url = `${process.env.REACT_APP_API_BASE_URL}${ROUTES.GET_PATIENT_PROFILE(patientId)}`;
                const response = await axios.get(url, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    }
                });
                const patient = response.data;
                setPatient({
                    name: patient.user.name,
                    age: patient.age,
                    gender: patient.gender,
                    contact_number: patient.contact_number,
                });
            } catch (error) {
                console.error(MESSAGES.FETCH_PATIENT_PROFILE_ERROR, error);
            } finally {
                setLoading(false);
            }
        };

        fetchPatientProfile();
    }, [patientId, token]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setPatient({ ...patient, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);

        const loader = new Noty({
            text: MESSAGES.BOOKING_APPOINTMENT,
            type: 'info',
            layout: 'topRight',
            theme: 'mint',
            timeout: false,
            progressBar: true,
            closeWith: []
        }).show();

        try {
            await axios.put(`${process.env.REACT_APP_API_BASE_URL}${ROUTES.UPDATE_PROFILE(patientId)}`,
                {
                    user: {
                        name: patient.name
                    },
                    age: patient.age,
                    gender: patient.gender.charAt(0).toUpperCase() + patient.gender.slice(1).toLowerCase(),
                    contact_number: patient.contact_number,
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    }
                });

            loader.close();
            new Noty({
                text: MESSAGES.UPDATE_PROFILE_SUCCESS,
                type: 'success',
                layout: 'topRight',
                theme: 'mint',
                timeout: 3000
            }).show();
        } catch (error) {
            loader.close();
            new Noty({
                text: MESSAGES.UPDATE_PROFILE_ERROR,
                type: 'error',
                layout: 'topRight',
                theme: 'mint',
                timeout: 3000
            }).show();
            console.error(MESSAGES.UPDATE_PROFILE_ERROR, error);
        } finally {
            setLoading(false);
        }
    };

    return (
      <div>
        <div className='patient-profile-container'>
            <Navbar />
            <div className="patient-profile">
                <h2 className="patient-profile__title">{isEditing ? TITLES.EDIT_PROFILE : TITLES.VIEW_PROFILE}</h2>
                <form className="patient-profile__form" onSubmit={handleSubmit}>
                    <div className="patient-profile__form-group">
                        <label htmlFor="name" className="patient-profile__label">
                            <FontAwesomeIcon icon={faUser} /> {LABELS.FULL_NAME}
                        </label>
                        <input
                            type="text"
                            id="name"
                            name="name"
                            className="patient-profile__input"
                            value={patient.name}
                            onChange={handleChange}
                            disabled={!isEditing}
                            required
                        />
                    </div>
                    <div className="patient-profile__form-group">
                        <label htmlFor="age" className="patient-profile__label">
                            <FontAwesomeIcon icon={faCalendarAlt} /> {LABELS.AGE}
                        </label>
                        <input
                            type="number"
                            id="age"
                            name="age"
                            className="patient-profile__input"
                            value={patient.age}
                            onChange={handleChange}
                            disabled={!isEditing}
                            required
                        />
                    </div>
                    <div className="patient-profile__form-group">
                        <label htmlFor="gender" className="patient-profile__label">
                            <FontAwesomeIcon icon={faVenusMars} /> {LABELS.GENDER}
                        </label>
                        <select
                            id="gender"
                            name="gender"
                            className="patient-profile__select"
                            value={patient.gender}
                            onChange={handleChange}
                            disabled={!isEditing}
                            required
                        >
                            {isEditing && <option value="">{LABELS.SELECT}</option>}
                            {!isEditing && <option value="">{patient.gender}</option>}
                            <option value="male">{GENDER_OPTIONS.MALE}</option>
                            <option value="female">{GENDER_OPTIONS.FEMALE}</option>
                            <option value="other">{GENDER_OPTIONS.OTHER}</option>
                        </select>
                    </div>
                    <div className="patient-profile__form-group">
                        <label htmlFor="contactNumber" className="patient-profile__label">
                            <FontAwesomeIcon icon={faPhone} /> {LABELS.CONTACT_NUMBER}
                        </label>
                        <input
                            type="tel"
                            id="contactNumber"
                            name="contactNumber"
                            className="patient-profile__input"
                            defaultValue={patient.contact_number}
                            onChange={handleChange}
                            disabled={!isEditing}
                            required
                        />
                    </div>
                    <div className="patient-profile__actions">
                        {!isEditing && (
                            <button type="button" className="patient-profile__edit-button" onClick={handleEditClick}>
                                <FontAwesomeIcon icon={faEdit} /> {BUTTON_TEXT.EDIT}
                            </button>
                        )}
                        {isEditing && (
                            <button type="submit" className="patient-profile__save-button">
                                <FontAwesomeIcon icon={faSave} /> {BUTTON_TEXT.SAVE}
                            </button>
                        )}
                        {isEditing && (
                            <button type="button" className="patient-profile__cancel-button" onClick={handleCancelClick}>
                                <FontAwesomeIcon icon={faTimes} /> {BUTTON_TEXT.CANCEL}
                            </button>
                        )}
                    </div>
                </form>
            </div>
            <Footer />
        </div>
        <Footer />
      </div>
    );
};

export default EditPatientProfile;
