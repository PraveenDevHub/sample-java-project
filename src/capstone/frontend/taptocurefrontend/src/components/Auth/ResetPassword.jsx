import React, { useState, useEffect } from 'react';
import axios from '../../axiosConfig';
import { useLocation, useNavigate } from 'react-router-dom';
import { FaEye, FaEyeSlash, FaCheckCircle, FaExclamationCircle } from 'react-icons/fa';
import '../../css/ResetPassword.css';
import Navbar from "../Subcomponents/Navbar";
import { RESET_PASSWORD_URL, MESSAGES, LABELS, PATHS } from "./constants";

const ResetPassword = () => {
    const query = new URLSearchParams(useLocation().search);
    const token = query.get('token');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [lengthValid, setLengthValid] = useState(false);
    const [caseValid, setCaseValid] = useState(false);
    const [numberValid, setNumberValid] = useState(false);
    const [error, setError] = useState({});
    const [toast, setToast] = useState({ message: '', type: '' });
    const [passwordVisible, setPasswordVisible] = useState(false);
    const [confirmPasswordVisible, setConfirmPasswordVisible] = useState(false);
    const [passwordFocused, setPasswordFocused] = useState(false);

    const navigate = useNavigate();

    useEffect(() => {
        if (toast.message) {
            const timer = setTimeout(() => {
                setToast({ message: '', type: '' });
            }, 5000); // 5000 milliseconds = 5 seconds

            return () => clearTimeout(timer); // Cleanup the timer on component unmount or when toast changes
        }
    }, [toast]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        if (name === 'newPassword') {
            setNewPassword(value);
            const validationErrors = validatePassword(value);
            setError(validationErrors);
        } else if (name === 'confirmPassword') {
            setConfirmPassword(value);
            if (value !== newPassword) {
                setError({ ...error, confirmPassword: MESSAGES.PASSWORDS_DO_NOT_MATCH });
                console.log(error);
                
            } else {
                const { confirmPassword, ...rest } = error;
                setError(rest);
            }
        }
    };

    const validatePassword = (password) => {
        const errors = {};
        setLengthValid(password.length >= 8);
        setCaseValid(/[a-z]/.test(password) && /[A-Z]/.test(password));
        setNumberValid(/[0-9]/.test(password) || /[!@#$%^&*]/.test(password));

        if (password.length < 8) errors.length = LABELS.PASSWORD_VALIDATION.LENGTH;
        if (!/[a-z]/.test(password) || !/[A-Z]/.test(password)) errors.case = LABELS.PASSWORD_VALIDATION.CASE;
        if (!/[0-9]/.test(password) && !/[!@#$%^&*]/.test(password)) errors.number = LABELS.PASSWORD_VALIDATION.NUMBER;
        return errors;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (newPassword !== confirmPassword) {
            setError({ confirmPassword: MESSAGES.PASSWORDS_DO_NOT_MATCH });
            setToast({ message: MESSAGES.PASSWORDS_DO_NOT_MATCH, type: 'error' });
            return;
        }
        try {
            await axios.post(RESET_PASSWORD_URL, { token, newPassword });
            setToast({ message: MESSAGES.PASSWORD_RESET_SUCCESS, type: 'success' });
            navigate(PATHS.LOGIN);
        } catch (error) {
            setError({ general: MESSAGES.ERROR_RESETTING_PASSWORD });
            setToast({ message: MESSAGES.ERROR_RESETTING_PASSWORD, type: 'error' });
        }
    };

    return (
        <div>
            <Navbar />
            <div className="reset-password-page-background">
                <div className="reset-password-container">
                    <h2 className="reset-password-title">{LABELS.RESET_PASSWORD}</h2>
                    <form onSubmit={handleSubmit} className="reset-password-form">
                        <div className="reset-password-form-group">
                            <div className="reset-password-input-group">
                                <input 
                                    type={passwordVisible ? "text" : "password"} 
                                    className={`reset-password-form-control ${error.newPassword ? 'reset-password-error' : ''}`} 
                                    name="newPassword" 
                                    value={newPassword} 
                                    onChange={handleChange} 
                                    placeholder={LABELS.NEW_PASSWORD} 
                                    required 
                                    onFocus={() => setPasswordFocused(true)}
                                    onBlur={() => setPasswordFocused(false)}
                                />
                                <button type="button" className="reset-password-btn-outline-secondary" onClick={() => setPasswordVisible(!passwordVisible)}>
                                    {passwordVisible ? <FaEye /> : <FaEyeSlash />}
                                </button>
                            </div>
                            {passwordFocused && (
                                <div className="form-text">
                                    <ul className="validation-list">
                                        <li className={lengthValid ? 'text-success' : 'text-danger'}>
                                            {LABELS.PASSWORD_VALIDATION.LENGTH}
                                        </li>
                                        <li className={caseValid ? 'text-success' : 'text-danger'}>
                                            {LABELS.PASSWORD_VALIDATION.CASE}
                                        </li>
                                        <li className={numberValid ? 'text-success' : 'text-danger'}>
                                            {LABELS.PASSWORD_VALIDATION.NUMBER}
                                        </li>
                                    </ul>
                                </div>
                            )}
                        </div>
                        <div className="reset-password-form-group">
                            <div className="reset-password-input-group">
                                <input 
                                    type={confirmPasswordVisible ? "text" : "password"} 
                                    className={`reset-password-form-control ${error.confirmPassword ? 'reset-password-error' : ''}`} 
                                    name="confirmPassword" 
                                    value={confirmPassword} 
                                    onChange={handleChange} 
                                    placeholder={LABELS.CONFIRM_PASSWORD} 
                                    required 
                                />
                                <button type="button" className="reset-password-btn-outline-secondary" onClick={() => setConfirmPasswordVisible(!confirmPasswordVisible)}>
                                    {confirmPasswordVisible ? <FaEye /> : <FaEyeSlash />}
                                </button>
                            </div>
                            {error.confirmPassword && <div className="reset-password-error-message">{error.confirmPassword}</div>}
                        </div>
                        <button type="submit" className="reset-password-btn-primary">{LABELS.RESET_PASSWORD}</button>
                    </form>
                </div>
                {toast.message && (
                    <div className={`reset-password-toast ${toast.type === 'success' ? 'reset-password-toast-success' : 'reset-password-toast-error'}`}>
                        <span className="reset-password-toast-icon">
                            {toast.type === 'success' ? <FaCheckCircle /> : <FaExclamationCircle />}
                        </span>
                        {toast.message}
                    </div>
                )}
            </div>
        </div>
    );
};

export default ResetPassword;
