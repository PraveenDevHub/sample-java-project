import React, { useState, useEffect } from "react";
import axios from "../../axiosConfig";
import { useNavigate } from "react-router-dom";
import {
  FaEye,
  FaEyeSlash,
  FaCheckCircle,
  FaExclamationCircle,
} from "react-icons/fa";
import "../../css/Signup.css";
import Navbar from "../Subcomponents/Navbar";
import Footer from "../Subcomponents/Footer";
import { REGISTER_URL, MESSAGES, LABELS, SPECIALIZATIONS, GENDERS, PATHS } from "./constants";
import { statesAndCities } from "../../config/statesAndCities";

const Signup = () => {
  const [user, setUser] = useState({
    name: "",
    email: "",
    password: "",
    role: "",
  });
  const [roleSpecificData, setRoleSpecificData] = useState({});
  const [lengthValid, setLengthValid] = useState(false);
  const [caseValid, setCaseValid] = useState(false);
  const [numberValid, setNumberValid] = useState(false);
  const [errors, setErrors] = useState({});
  const [confirmPassword, setConfirmPassword] = useState("");
  const [passwordVisible, setPasswordVisible] = useState(false);
  const [passwordFocused, setPasswordFocused] = useState(false);
  const [confirmPasswordVisible, setConfirmPasswordVisible] = useState(false);
  const [selectedState, setSelectedState] = useState("");
  const [toast, setToast] = useState({ message: "", type: "" });
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    const checkVerificationStatus = async () => {
      try {
        const email = user.email;
        if (email) {
          const response = await axios.get(
            `/check-verification?email=${email}`
          );
          if (response.data === true) {
            navigate(PATHS.LOGIN);
          }
        }
      } catch (error) {
        console.error(MESSAGES.ERROR_CHECKING_VERIFICATION_STATUS, error);
      }
    };

    checkVerificationStatus();
  }, [user.email, navigate]);

  useEffect(() => {
    if (toast.message) {
      const timer = setTimeout(() => {
        setToast({ message: "", type: "" });
      }, 5000); // 5000 milliseconds = 5 seconds

      return () => clearTimeout(timer); // Cleanup the timer on component unmount or when toast changes
    }
  }, [toast]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUser({ ...user, [name]: value });

    if (name === "password") {
      const validationErrors = validatePassword(value);
      setErrors(validationErrors);
    }

    if (name === "name") {
      if (!/^[a-zA-Z\s]*$/.test(value)) {
        setErrors({
          ...errors,
          name: MESSAGES.NAME_VALIDATION,
        });
      } else {
        const { name, ...rest } = errors;
        setErrors(rest);
      }
    }

    if (name === "email") {
      if (!validateEmail(value)) {
        setErrors({ ...errors, email: MESSAGES.EMAIL_VALIDATION });
      } else {
        const { email, ...rest } = errors;
        setErrors(rest);
      }
    }
  };

  const handleRoleSpecificChange = (e) => {
    const { name, value } = e.target;
    setRoleSpecificData({ ...roleSpecificData, [name]: value });

    if (name === "yearsOfExperience" || name === "age") {
      if (!/^[1-9]\d?$/.test(value)) {
        setErrors({
          ...errors,
          [name]: name === "yearsOfExperience" ? MESSAGES.YEARS_OF_EXPERIENCE_VALIDATION : MESSAGES.AGE_VALIDATION,
        });
      } else {
        const { [name]: removedError, ...rest } = errors;
        setErrors(rest);
      }
    }

    if (name === "contact_number") {
      if (!/^\d*$/.test(value)) {
        setErrors({
          ...errors,
          contact_number: MESSAGES.CONTACT_NUMBER_VALIDATION,
        });
      } else if (value.length !== 10) {
        setErrors({
          ...errors,
          contact_number: MESSAGES.CONTACT_NUMBER_LENGTH_VALIDATION,
        });
      } else {
        const { contact_number, ...rest } = errors;
        setErrors(rest);
      }
    }

    if (name === "hospitalName") {
      if (!/^[a-zA-Z\s]*$/.test(value)) {
        setErrors({
          ...errors,
          hospitalName: MESSAGES.HOSPITAL_NAME_VALIDATION,
        });
      } else {
        const { hospitalName, ...rest } = errors;
        setErrors(rest);
      }
    }
  };

  const validatePassword = (password) => {
    const errors = {};
    setLengthValid(password.length >= 8);
    setCaseValid(/[a-z]/.test(password) && /[A-Z]/.test(password));
    setNumberValid(/[0-9]/.test(password) || /[!@#$%^&*]/.test(password));

    if (password.length < 8)
      errors.length = LABELS.PASSWORD_VALIDATION.LENGTH;
    if (!/[a-z]/.test(password) || !/[A-Z]/.test(password))
      errors.case = LABELS.PASSWORD_VALIDATION.CASE;
    if (!/[0-9]/.test(password) && !/[!@#$%^&*]/.test(password))
      errors.number = LABELS.PASSWORD_VALIDATION.NUMBER;
    return errors;
  };

  const validateEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    if (user.password !== confirmPassword) {
      setToast({ message: MESSAGES.PASSWORDS_DO_NOT_MATCH, type: 'error' });
      setErrors({ confirmPassword: MESSAGES.PASSWORDS_DO_NOT_MATCH });
      setLoading(false);
      return;
    }

    if (!validateEmail(user.email)) {
      setToast({ message: MESSAGES.EMAIL_VALIDATION, type: 'error' });
      setErrors({ email: MESSAGES.EMAIL_VALIDATION });
      setLoading(false);
      return;
    }

    if (!lengthValid || !caseValid || !numberValid) {
      setToast({
        message: MESSAGES.PASSWORD_CRITERIA_NOT_MET,
        type: 'error',
      });
      setLoading(false);
      return;
    }

    if (Object.keys(errors).length > 0) {
      setToast({
        message: MESSAGES.ENTER_VALID_DETAILS,
        type: 'error',
      });
      setLoading(false);
      return;
    }

    try {
      const payload = { user, roleSpecificData };
      await axios.post(REGISTER_URL, payload);
      setToast({
        message: MESSAGES.REGISTRATION_SUCCESS,
        type: 'success',
      });
    } catch (error) {
      if (error.response && error.response.status === 409) {
        setToast({
          message: MESSAGES.EMAIL_ALREADY_EXISTS,
          type: 'error',
        });
        setErrors({
          email: MESSAGES.EMAIL_ALREADY_EXISTS,
        });
      } else {
        console.error(MESSAGES.ERROR_DURING_REGISTRATION, error);
        setToast({
          message: MESSAGES.REGISTRATION_FAILED,
          type: 'error',
        });
      }
    } finally {
      setLoading(false);
    }
  };

  const handleStateChange = (e) => {
    const { value } = e.target;
    setSelectedState(value);
    setRoleSpecificData({ ...roleSpecificData, state: value, city: "" });
  };

  return (
    <div>
      <Navbar />
      <div className="signup-page-background">
        <div className="signup-container mb-5">
          <h2 className="signup-header">{LABELS.SIGN_UP}</h2>
          <form className="signup-form" onSubmit={handleSubmit}>
            <div className="mb-3">
              <input
                type="text"
                className={`signup-form-input ${errors.name ? "signup-error-highlight" : ""
                  }`}
                name="name"
                value={user.name}
                onChange={handleChange}
                placeholder={LABELS.NAME}
                required
              />
              {errors.name && (
                <div className="signup-error-message">{errors.name}</div>
              )}
            </div>
            <div className="mb-3">
              <input
                type="email"
                className={`signup-form-input ${errors.email ? "signup-error-highlight" : ""
                  }`}
                name="email"
                value={user.email}
                onChange={handleChange}
                placeholder={LABELS.EMAIL}
                required
              />
              {errors.email && (
                <div className="signup-error-message">{errors.email}</div>
              )}
            </div>
            <div className="mb-3">
              <div className="input-group">
                <input
                  type={passwordVisible ? "text" : "password"}
                  className={`signup-form-input ${errors.password ? "signup-error-highlight" : ""
                    }`}
                  name="password"
                  value={user.password}
                  onChange={handleChange}
                  placeholder={LABELS.PASSWORD}
                  required
                  onFocus={() => setPasswordFocused(true)}
                  onBlur={() => setPasswordFocused(false)}
                />
                <button
                  type="button"
                  className="btn"
                  onClick={() => setPasswordVisible(!passwordVisible)}
                >
                  {passwordVisible ? <FaEye /> : <FaEyeSlash />}
                </button>
              </div>
              {passwordFocused && (
                <div className="form-text">
                  <ul className="validation-list">
                    <li
                      className={lengthValid ? "text-success" : "text-danger"}
                    >
                      {LABELS.PASSWORD_VALIDATION.LENGTH}
                    </li>
                    <li className={caseValid ? "text-success" : "text-danger"}>
                      {LABELS.PASSWORD_VALIDATION.CASE}
                    </li>
                    <li
                      className={numberValid ? "text-success" : "text-danger"}
                    >
                      {LABELS.PASSWORD_VALIDATION.NUMBER}
                    </li>
                  </ul>
                </div>
              )}
            </div>
            <div className="mb-3">
              <div className="input-group">
                <input
                  type={confirmPasswordVisible ? "text" : "password"}
                  className={`signup-form-input ${errors.confirmPassword ? "signup-error-highlight" : ""
                    }`}
                  name="confirmPassword"
                  value={confirmPassword}
                  onChange={(e) => {
                    setConfirmPassword(e.target.value);
                    if (e.target.value !== user.password) {
                      setErrors({
                        ...errors,
                        confirmPassword: MESSAGES.PASSWORDS_DO_NOT_MATCH,
                      });
                    } else {
                      const { confirmPassword, ...rest } = errors;
                      setErrors(rest);
                    }
                  }}
                  placeholder={LABELS.CONFIRM_PASSWORD}
                  required
                />
                <button
                  type="button"
                  className="btn"
                  onClick={() =>
                    setConfirmPasswordVisible(!confirmPasswordVisible)
                  }
                >
                  {confirmPasswordVisible ? <FaEye /> : <FaEyeSlash />}
                </button>
              </div>
              {errors.confirmPassword && (
                <div className="signup-error-message">
                  {errors.confirmPassword}
                </div>
              )}
            </div>
            <div className="mb-3">
              <select
                className={`signup-form-select ${errors.role ? "signup-error-highlight" : ""
                  }`}
                name="role"
                value={user.role}
                onChange={handleChange}
                required
              >
                <option value="" disabled>
                  {LABELS.SELECT_ROLE}
                </option>
                <option value="Patient">{LABELS.PATIENT}</option>
                <option value="Doctor">{LABELS.DOCTOR}</option>
              </select>
            </div>
            {user.role === "Doctor" && (
              <>
                <div className="mb-3">
                  <input
                    type="text"
                    className={`signup-form-input ${errors.medicalLicenseNumber
                      ? "signup-error-highlight"
                      : ""
                      }`}
                    name="medicalLicenseNumber"
                    onChange={handleRoleSpecificChange}
                    placeholder={LABELS.MEDICAL_LICENSE_NUMBER}
                    required
                  />
                  {errors.medicalLicenseNumber && (
                    <div className="signup-error-message">
                      {errors.medicalLicenseNumber}
                    </div>
                  )}
                </div>
                <div className="mb-3">
                  <select
                    className={`signup-form-select ${errors.specialization ? "signup-error-highlight" : ""
                      }`}
                    name="specialization"
                    onChange={handleRoleSpecificChange}
                    required
                  >
                    <option value="">{LABELS.SPECIALIZATION}</option>
                    {SPECIALIZATIONS.map((specialization) => (
                      <option key={specialization} value={specialization}>
                        {specialization}
                      </option>
                    ))}
                  </select>
                  {errors.specialization && (
                    <div className="signup-error-message">
                      {errors.specialization}
                    </div>
                  )}

                </div>
                <div className="mb-3">
                  <input
                    type="number"
                    className={`signup-form-input ${errors.yearsOfExperience ? "signup-error-highlight" : ""
                      }`}
                    name="yearsOfExperience"
                    onChange={handleRoleSpecificChange}
                    placeholder={LABELS.YEARS_OF_EXPERIENCE}
                    min="1"
                    required
                  />
                  {errors.yearsOfExperience && (
                    <div className="signup-error-message">
                      {errors.yearsOfExperience}
                    </div>
                  )}
                </div>
                <div className="mb-3">
                  <input
                    type="text"
                    className={`signup-form-input ${errors.hospitalName ? "signup-error-highlight" : ""
                      }`}
                    name="hospitalName"
                    onChange={handleRoleSpecificChange}
                    placeholder={LABELS.HOSPITAL_NAME}
                    required
                  />
                  {errors.hospitalName && (
                    <div className="signup-error-message">
                      {errors.hospitalName}
                    </div>
                  )}
                </div>
                <div className="mb-3">
                  <div className="row">
                    <div className="col">
                      <select
                        className={`signup-form-select ${errors.state ? "signup-error-highlight" : ""
                          }`}
                        name="state"
                        onChange={handleStateChange}
                        required
                      >
                        <option value="">{LABELS.STATE}</option>
                        {Object.keys(statesAndCities).map((state) => (
                          <option key={state} value={state}>
                            {state}
                          </option>
                        ))}
                      </select>
                      {errors.state && (
                        <div className="signup-error-message">
                          {errors.state}
                        </div>
                      )}
                    </div>
                    <div className="col">
                      <select
                        className={`signup-form-select ${errors.city ? "signup-error-highlight" : ""
                          }`}
                        name="city"
                        value={roleSpecificData.city || ""}
                        onChange={handleRoleSpecificChange}
                        required
                      >
                        <option value="">{LABELS.CITY}</option>
                        {selectedState &&
                          statesAndCities[selectedState].map((city) => (
                            <option key={city} value={city}>
                              {city}
                            </option>
                          ))}
                      </select>
                      {errors.city && (
                        <div className="signup-error-message">
                          {errors.city}
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              </>
            )}
            {user.role === "Patient" && (
              <>
                <div className="mb-3">
                  <input
                    type="number"
                    className={`signup-form-input ${errors.age ? "signup-error-highlight" : ""
                      }`}
                    name="age"
                    onChange={handleRoleSpecificChange}
                    placeholder={LABELS.AGE}
                    min="1"
                    required
                  />
                  {errors.age && (
                    <div className="signup-error-message">{errors.age}</div>
                  )}
                </div>
                <div className="mb-3">
                  <select
                    className={`signup-form-select ${errors.gender ? "signup-error-highlight" : ""
                      }`}
                    name="gender"
                    onChange={handleRoleSpecificChange}
                    required
                  >
                    <option value="">{LABELS.GENDER}</option>
                    {GENDERS.map((gender) => (
                      <option key={gender} value={gender}>
                        {gender}
                      </option>
                    ))}
                  </select>
                  {errors.gender && (
                    <div className="signup-error-message">{errors.gender}</div>
                  )}

                </div>
                <div className="mb-3">
                  <input
                    type="text"
                    className={`signup-form-input ${errors.contact_number ? "signup-error-highlight" : ""
                      }`}
                    name="contact_number"
                    onChange={handleRoleSpecificChange}
                    placeholder={LABELS.CONTACT_NUMBER}
                    required
                  />
                  {errors.contact_number && (
                    <div className="signup-error-message">
                      {errors.contact_number}
                    </div>
                  )}
                </div>
              </>
            )}
            <button type="submit" className="signup-button" disabled={loading}>
              {loading ? LABELS.SIGNING_UP : LABELS.SIGN_UP_BUTTON}
            </button>
            {loading && (
              <div className="signup-loading">
                <span></span>
                <span></span>
                <span></span>
              </div>
            )}
            <div className="signup-login-link">
              {LABELS.ALREADY_HAVE_ACCOUNT} <a href={PATHS.LOGIN}>{LABELS.LOGIN}</a>
            </div>
          </form>
        </div>
        {toast.message && (
          <div
            className={`signup-toast ${toast.type === "success"
              ? "signup-toast-success"
              : "signup-toast-error"
              }`}
          >
            <span className="signup-toast-icon">
              {toast.type === "success" ? (
                <FaCheckCircle />
              ) : (
                <FaExclamationCircle />
              )}
            </span>
            {toast.message}
          </div>
        )}
      </div>
      <Footer />
    </div>
  );
};

export default Signup;
