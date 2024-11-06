import React, { useState, useEffect } from "react";
import "../../css/PatientHome.css";
import axiosInstance from "../../axiosConfig";
import Footer from "../Subcomponents/Footer";
import Navbar from "../Subcomponents/Navbar";
import { useNavigate } from "react-router-dom";
import { LABELS, MESSAGES, ROUTES, BUTTON_TEXT, TITLES } from './constants';

const PatientHome = () => {
  const [selectedState, setSelectedState] = useState("");
  const [selectedCity, setSelectedCity] = useState("");
  const [selectedSpecialization, setSelectedSpecialization] = useState("");
  const [selectedHospital, setSelectedHospital] = useState("");
  const [states, setStates] = useState([]);
  const [cities, setCities] = useState([]);
  const [specializations, setSpecializations] = useState([]);
  const [hospitals, setHospitals] = useState([]);
  const [filteredDoctors, setFilteredDoctors] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [showFilters, setShowFilters] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    axiosInstance
      .get("/patient/doctors?limit=6")
      .then((response) => {
        console.log("Fetched doctors:", response.data);
        setFilteredDoctors(response.data);
      })
      .catch((error) => {
        console.error(MESSAGES.FETCH_DOCTORS_ERROR, error);
      });

    axiosInstance
      .get("/patient/doctors/filter")
      .then((response) => {
        const doctors = response.data;
        console.log("Fetched doctors for filters:", doctors);
        const uniqueStates = [
          ...new Set(doctors.map((doctor) => doctor.state)),
        ];
        const uniqueSpecializations = [
          ...new Set(doctors.map((doctor) => doctor.specialization)),
        ];
        setStates(uniqueStates);
        setSpecializations(uniqueSpecializations);
      })
      .catch((error) => {
        console.error(MESSAGES.FETCH_DOCTORS_ERROR, error);
      });
  }, []);

  useEffect(() => {
    if (selectedState) {
      axiosInstance
        .get(`/patient/doctors/filter?state=${selectedState}`)
        .then((response) => {
          const doctors = response.data;
          const filteredCities = doctors.map((doctor) => doctor.city);
          setCities([...new Set(filteredCities)]);
        })
        .catch((error) => {
          console.error(MESSAGES.FETCH_CITIES_ERROR, error);
        });
    } else {
      setCities([]);
    }
  }, [selectedState]);

  useEffect(() => {
    if (selectedCity) {
      axiosInstance
        .get(`/patient/doctors/filter?city=${selectedCity}`)
        .then((response) => {
          const doctors = response.data;
          const filteredHospitals = doctors.map(
            (doctor) => doctor.hospitalName
          );
          setHospitals([...new Set(filteredHospitals)]);
        })
        .catch((error) => {
          console.error(MESSAGES.FETCH_HOSPITALS_ERROR, error);
        });
    } else {
      setHospitals([]);
    }
  }, [selectedCity]);

  const handleSearch = () => {
    axiosInstance
      .get("/patient/doctors/filter", {
        params: {
          state: selectedState,
          city: selectedCity,
          specialization: selectedSpecialization,
          hospital: selectedHospital,
          searchTerm: searchTerm,
        },
      })
      .then((response) => {
        setFilteredDoctors(response.data);
        setShowFilters(false);
      })
      .catch((error) => {
        console.error(MESSAGES.FETCH_FILTERED_DOCTORS_ERROR, error);
      });
  };

  const handleClearFilters = () => {
    setSelectedState("");
    setSelectedCity("");
    setSelectedSpecialization("");
    setSelectedHospital("");
    setSearchTerm("");
    setFilteredDoctors([]);
  };

  const handleSearchTermChange = (e) => {
    setSearchTerm(e.target.value);
    handleSearch();
  };

  const handleClickOutside = (e) => {
    if (e.target.className === "patienthome-filters-popup") {
      setShowFilters(false);
    }
  };

  const handleBookAppointment = (doctorId) => {
    navigate(`/bookappointment?doctorId=${doctorId}`);
  };

  return (
    <div className="page-background">
      <div className="mb-5">
        <Navbar />
      </div>
      <div className="patienthome-container">
        <div className="patienthome-header">
          <div className="search-and-filters">
            <input
              type="text"
              placeholder={LABELS.SEARCH_BY_NAME_OR_SPECIALIZATION}
              value={searchTerm}
              onChange={handleSearchTermChange}
              className="patienthome-search-input"
            />
            <button
              className="filter-button"
              onClick={() => setShowFilters(!showFilters)}
            >
              {BUTTON_TEXT.FILTERS}
            </button>
          </div>
        </div>
        {filteredDoctors.length === 0 && (
          <p className="no-doctors-found">{LABELS.NO_DOCTORS_FOUND}</p>
        )}
        {showFilters && (
          <div
            className="patienthome-filters-popup"
            onClick={handleClickOutside}
          >
            <div className="patienthome-filters">
              <h3>{BUTTON_TEXT.FILTERS}</h3>
              <div className="patienthome-location">
                <div className="patienthome-dropdown">
                  <label htmlFor="state">{LABELS.STATE}</label>
                  <select
                    id="state"
                    value={selectedState}
                    onChange={(e) => setSelectedState(e.target.value)}
                    className="patienthome-select"
                  >
                    <option value="">{LABELS.SELECT}</option>
                    {states.map((state) => (
                      <option key={state} value={state}>
                        {state}
                      </option>
                    ))}
                  </select>
                </div>
                <div className="patienthome-dropdown">
                  <label htmlFor="city">{LABELS.CITY}</label>
                  <select
                    id="city"
                    value={selectedCity}
                    onChange={(e) => setSelectedCity(e.target.value)}
                    className="patienthome-select"
                    disabled={!selectedState}
                  >
                    <option value="">{LABELS.SELECT}</option>
                    {cities.map((city) => (
                      <option key={city} value={city}>
                        {city}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
              <div className="patienthome-dropdown">
                <label htmlFor="specialization">{LABELS.SPECIALIZATION}</label>
                <select
                  id="specialization"
                  value={selectedSpecialization}
                  onChange={(e) => setSelectedSpecialization(e.target.value)}
                  className="patienthome-select"
                >
                  <option value="">{LABELS.SELECT}</option>
                  {specializations.map((specialization) => (
                    <option key={specialization} value={specialization}>
                      {specialization}
                    </option>
                  ))}
                </select>
              </div>
              <div className="patienthome-dropdown">
                <label htmlFor="hospital">{LABELS.HOSPITAL}</label>
                <select
                  id="hospital"
                  value={selectedHospital}
                  onChange={(e) => setSelectedHospital(e.target.value)}
                  className="patienthome-select"
                  disabled={!selectedCity}
                >
                  <option value="">{LABELS.SELECT}</option>
                  {hospitals.map((hospital) => (
                    <option key={hospital} value={hospital}>
                      {hospital}
                    </option>
                  ))}
                </select>
              </div>
              <div className="patienthome-buttons">
                <button onClick={handleSearch} className="patienthome-button">
                  {BUTTON_TEXT.SEARCH}
                </button>
                <button
                  onClick={handleClearFilters}
                  className="patienthome-button"
                >
                  {BUTTON_TEXT.CLEAR_FILTERS}
                </button>
              </div>
            </div>
          </div>
        )}
        <div className="patienthome-results">
          {filteredDoctors.length > 0 &&
            filteredDoctors.map((doctor) => (
              <div key={doctor.id} className="patienthome-doctor-card">
                <div className="doctor-card-left">
                  <img
                    src={doctor.profilePhoto}
                    alt={`Dr. ${doctor.user.name}`}
                    className="doctor-profile-photo"
                  />
                </div>
                <div className="doctor-card-right">
                  <h4 className="card-title">Dr. {doctor.user.name}</h4>
                  <h5 className="card-subtitle mb-2 text-muted">
                    {doctor.specialization}
                  </h5>
                  <p className="card-text">
                    <strong>{LABELS.HOSPITAL}</strong> {doctor.hospitalName}
                  </p>
                  <p className="card-text">
                    <strong>{LABELS.EXPERIENCE}</strong> {doctor.yearsOfExperience} years
                  </p>
                  <p className="card-text">
                    <strong>{LABELS.ADDRESS}</strong> {doctor.city}, {doctor.state}
                  </p>
                  <button
                    className="btn btn-outline-success book-appointment-button"
                    onClick={() => handleBookAppointment(doctor.doctorId)}
                  >
                    {LABELS.BOOK_APPOINTMENT_BUTTON}
                  </button>
                </div>
              </div>
            ))}
        </div>
      </div>
      <div className="mt-5">
        <Footer />
      </div>
    </div>
  );
};

export default PatientHome;
