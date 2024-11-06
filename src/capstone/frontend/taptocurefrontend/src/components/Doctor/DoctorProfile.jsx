import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser, faEnvelope, faIdCard, faStethoscope, faCalendarAlt, faHospital, faSave } from '@fortawesome/free-solid-svg-icons';
import Noty from 'noty';
import 'noty/lib/noty.css';
import '../../css/doctorprofile.css';
import { useSelector } from 'react-redux';
import Navbar from '../Subcomponents/Navbar';
import Footer from '../Subcomponents/Footer';
import { FaEdit, FaTimes } from 'react-icons/fa';
import { statesAndCities } from '../../config/statesAndCities';
import {
  UPDATE_DOCTOR_PROFILE_URL,
  GET_DOCTOR_PROFILE_URL,
  TOAST_MESSAGES,
  ERROR_MESSAGES,
  TITLES,
  TEXTS,
  BUTTON_LABELS,
  FORM_LABELS,
  LOG_MESSAGES,
} from './constants';

const DoctorProfile = () => {
    const token = useSelector((state) => state.auth.token);
    const doctorId = useSelector((state) => state.auth.doctorId);
    const userId = useSelector((state) => state.auth.userId);
    const [doctorData, setDoctorData] = useState({
      name: '',
      email: '',
      medicalLicenseNumber: '',
      specialization: '',
      yearsOfExperience: '',
      doctorDescription: '',
      profilePhoto: null,
      photoUrl: null,
      hospitalName: '',
      state: '',
      city: '',
      approvalStatus: '',
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [isEditing, setIsEditing] = useState(false);
  
    useEffect(() => {
      const fetchDoctorProfile = async () => {
        setLoading(true);
        try {
          const url = GET_DOCTOR_PROFILE_URL(doctorId);
          const response = await axios.get(url, {
            headers: {
              Authorization: `Bearer ${token}`,
            }
          });
          const doctor = response.data;
          const photoUrl = doctor.profilePhoto;
  
          setDoctorData((prevData) => ({
            ...prevData,
            name: doctor.user.name,
            email: doctor.user.email,
            medicalLicenseNumber: doctor.medicalLicenseNumber,
            specialization: doctor.specialization,
            yearsOfExperience: doctor.yearsOfExperience,
            doctorDescription: doctor.doctorDescription,
            photoUrl: photoUrl,
            hospitalName: doctor.hospitalName,
            state: doctor.state,
            city: doctor.city,
            approvalStatus: doctor.approvalStatus,
          }));
        } catch (error) {
          setError(error.response?.data?.message || ERROR_MESSAGES.FETCH_PROFILE_ERROR);
        } finally {
          setLoading(false);
        }
      };
  
      fetchDoctorProfile();
    }, [doctorId]);
  
    const handlePhotoChange = (event) => {
      const file = event.target.files[0];
      if (file) {
        setDoctorData((prevData) => ({
          ...prevData,
          profilePhoto: file,
          photoUrl: URL.createObjectURL(file),
        }));
      }
    };
  
    const handleSaveChanges = async (e) => {
      e.preventDefault();
      const formData = new FormData();
      formData.append('name', doctorData.name);
      formData.append('specialization', doctorData.specialization);
      formData.append('yearsOfExperience', doctorData.yearsOfExperience);
      formData.append('doctorDescription', doctorData.doctorDescription);
      formData.append('hospitalName', doctorData.hospitalName);
      formData.append('state', doctorData.state);
      formData.append('city', doctorData.city);
      if (doctorData.profilePhoto) {
        formData.append('profilePhoto', doctorData.profilePhoto);
      }
  
      const loader = new Noty({
        text: TEXTS.PLEASE_WAIT_SAVING,
        type: 'info',
        layout: 'topRight',
        theme: 'mint',
        timeout: false,
        progressBar: true,
        closeWith: []
      }).show();
      
      try {
        const response = await axios.put(
          `http://localhost:8098/doctor/updateDoctor/${doctorId}`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );
  
        loader.close();
        new Noty({
          text: TOAST_MESSAGES.PROFILE_UPDATE_SUCCESS,
          type: 'success',
          layout: 'topRight',
          theme: 'mint',
          timeout: 3000
        }).show();
  
        setIsEditing(false); // Switch back to view mode after saving
      } catch (error) {
        loader.close();
        new Noty({
          text: TOAST_MESSAGES.PROFILE_UPDATE_FAILURE,
          type: 'error',
          layout: 'topRight',
          theme: 'mint',
          timeout: 3000
        }).show();
        console.error(LOG_MESSAGES.ERROR_UPDATING_PROFILE, error);
      }
    };
  
    const handleStateChange = (e) => {
      const state = e.target.value;
      setDoctorData((prevData) => ({ ...prevData, state, city: '' }));
    };
  
    const handleCityChange = (e) => {
      const city = e.target.value;
      setDoctorData((prevData) => ({ ...prevData, city }));
    };
  
    const handleEditClick = () => {
      setIsEditing(true);
    };
  
    const handleCancelClick = () => {
      setIsEditing(false);
    };
  
    return (
      <div className='doctor-profile-container'>
        <Navbar />
        <div className="doctor-profile">
          {loading && <p>{TEXTS.LOADING}</p>}
          {error && <p className="error-message">{error}</p>}
          <div className="profile-photo-container">
            <h2>{isEditing ? TITLES.EDIT_PROFILE : TITLES.VIEW_PROFILE}</h2>
            {doctorData.photoUrl ? (
              <img src={doctorData.photoUrl} alt="Profile" className="profile-photo" />
            ) : (
              <div className="profile-photo">{TEXTS.NO_PHOTO_AVAILABLE}</div>
            )}
            {isEditing && (
              <div className="photo-buttons">
                <input
                  type="file"
                  accept="image/*"
                  id="photo-upload"
                  onChange={handlePhotoChange}
                  style={{ display: 'none' }}
                />
                <label htmlFor="photo-upload" className="change-photo-button"><FaEdit />
                  {BUTTON_LABELS.CHANGE_PHOTO}
                </label>
              </div>
            )}
          </div>
          <form onSubmit={handleSaveChanges} className='doctor-form'>
            <div className="editprofile-form-group">
              <label>
                <FontAwesomeIcon icon={faUser} /> {FORM_LABELS.FULL_NAME}:
              </label>
              <input
                type="text"
                value={doctorData.name}
                onChange={(e) => setDoctorData(prevData => ({ ...prevData, name: e.target.value }))}
                disabled={!isEditing}
                required
              />
            </div>
  
            <div className="editprofile-form-group">
              <label>
                <FontAwesomeIcon icon={faEnvelope} /> {FORM_LABELS.EMAIL}:
              </label>
              <input
                type="email"
                value={doctorData.email}
                readOnly
              />
            </div>
  
            <div className="editprofile-form-group">
              <label>
                <FontAwesomeIcon icon={faIdCard} /> {FORM_LABELS.MEDICAL_LICENSE_NUMBER}:
              </label>
              <input type="text" value={doctorData.medicalLicenseNumber} readOnly />
            </div>
  
            <div className="editprofile-form-group">
              <label>
                <FontAwesomeIcon icon={faStethoscope} /> {FORM_LABELS.SPECIALIZATION}:
              </label>
              <input
                type="text"
                value={doctorData.specialization}
                onChange={(e) => setDoctorData((prevData) => ({ ...prevData, specialization: e.target.value }))}
                disabled={!isEditing}
                required
              />
            </div>
  
            <div className="editprofile-form-group">
              <label>
                <FontAwesomeIcon icon={faCalendarAlt} /> {FORM_LABELS.YEARS_OF_EXPERIENCE}:
              </label>
              <input
                type="number"
                value={doctorData.yearsOfExperience}
                onChange={(e) => setDoctorData((prevData) => ({ ...prevData, yearsOfExperience: e.target.value }))}
                disabled={!isEditing}
                required
              />
            </div>
  
            <div className="editprofile-form-group">
              <label>
                <FontAwesomeIcon icon={faHospital} /> {FORM_LABELS.HOSPITAL_NAME}:
              </label>
              <input
                type="text"
                value={doctorData.hospitalName}
                onChange={(e) => setDoctorData((prevData) => ({ ...prevData, hospitalName: e.target.value }))}
                disabled={!isEditing}
                required
              />
            </div>
  
            <div className="editprofile-form-group">
              <label>{FORM_LABELS.HOSPITAL_STATE}:</label>
              <select value={doctorData.state} onChange={handleStateChange} disabled={!isEditing} required>
                <option value="">{FORM_LABELS.SELECT_STATE}</option>
                {Object.keys(statesAndCities).map((state) => (
                  <option key={state} value={state}>
                    {state}
                  </option>
                ))}
              </select>
            </div>
  
            <div className="editprofile-form-group">
              <label>{FORM_LABELS.HOSPITAL_CITY}:</label>
              <select value={doctorData.city} onChange={handleCityChange} disabled={!isEditing} required>
                <option value="">{FORM_LABELS.SELECT_CITY}</option>
                {doctorData.state && statesAndCities[doctorData.state].map((city) => (
                  <option key={city} value={city}>
                    {city}
                  </option>
                ))}
              </select>
            </div>
  
            <div className="editprofile-form-group">
            <label>{FORM_LABELS.PROFILE_DESCRIPTION}:</label>
            <textarea
              value={doctorData.doctorDescription}
              onChange={(e) => setDoctorData((prevData) => ({ ...prevData, doctorDescription: e.target.value }))}
              disabled={!isEditing}
              required
            />
          </div>

          {isEditing && <button type="submit" className="doctor-profile__save-button"><FontAwesomeIcon icon={faSave} /> {BUTTON_LABELS.SAVE} </button>}
          {isEditing && <button type="button" className="doctor-profile__cancel-button" onClick={handleCancelClick}> <FaTimes /> {BUTTON_LABELS.CANCEL}</button>}

          {!isEditing && <button type="button" className="edit-button" onClick={handleEditClick}><FontAwesomeIcon icon={FaEdit} /> {BUTTON_LABELS.EDIT_PROFILE}</button>}
        </form>
      </div>
      <Footer />
    </div>
  );
};

export default DoctorProfile;
