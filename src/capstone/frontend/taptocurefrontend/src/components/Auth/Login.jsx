import React, { useState, useEffect } from "react";
import { useDispatch } from "react-redux";
import { useNavigate, Link } from "react-router-dom";
import { setAuth } from "../../authSlice";
import axios from "../../axiosConfig";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../../css/Login.css";
import Navbar from "../Subcomponents/Navbar";
import Footer from "../Subcomponents/Footer";
import ForgotPasswordModal from "../Auth/ForgotPasswordModal";
import { LOGIN_URL, MESSAGES, LABELS, PATHS } from "./constants";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [passwordVisible, setPasswordVisible] = useState(false);
  const [showForgotPasswordModal, setShowForgotPasswordModal] = useState(false);

  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    if (error) {
      const timer = setTimeout(() => {
        setError("");
      }, 5000); // 5000 milliseconds = 5 seconds

      return () => clearTimeout(timer); // Cleanup the timer on component unmount or when error changes
    }
  }, [error]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(LOGIN_URL, { email, password });
      const { token, role, isVerified, name, userId, doctorId, patientId } =
        response.data; // Ensure doctorId and patientId are included in the response

      if (!isVerified) {
        toast.error(MESSAGES.VERIFY_EMAIL);
        setError(MESSAGES.VERIFY_EMAIL);
        return;
      }

      dispatch(setAuth({ token, role, name, userId, doctorId, patientId })); // Include doctorId and patientId in the payload
      toast.success(MESSAGES.LOGIN_SUCCESS);
      if (role === "Doctor") {
        navigate(PATHS.DOCTOR_HOME);
      } else if (role === "Patient") {
        navigate(PATHS.PATIENT_HOME);
      } else if (role === "Admin") {
        navigate(PATHS.ADMIN_HOME);
      }
    } catch (error) {
      if (error.response) {
        if (error.response.status === 401) {
          toast.error(MESSAGES.INVALID_CREDENTIALS);
          setError(MESSAGES.INVALID_CREDENTIALS);
        } else if (error.response.status === 403) {
          toast.error(MESSAGES.EMAIL_NOT_VERIFIED);
          setError(MESSAGES.EMAIL_NOT_VERIFIED);
        } else if (error.response.status === 404) {
          toast.error(MESSAGES.EMAIL_NOT_EXIST);
          setError(MESSAGES.EMAIL_NOT_EXIST);
        } else {
          toast.error(MESSAGES.LOGIN_FAILED);
          setError(MESSAGES.LOGIN_FAILED);
        }
      } else {
        console.error("Error during login:", error);
        toast.error(MESSAGES.LOGIN_FAILED);
        setError(MESSAGES.LOGIN_FAILED);
      }
    }
  };

  return (
    <div>
      <Navbar />
      <div className="login-page-background">
        <ToastContainer
          position="bottom-left"
          autoClose={5000}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          style={{ width: "300px", fontSize: "16px" }} // Adjust size and font size
        />
        <div className="login-container">
          <h2 className="login-title">{LABELS.LOGIN_TITLE}</h2>
          <form onSubmit={handleSubmit} className="login-form">
            <div className="login-form-group">
              <input
                type="email"
                className={`login-form-control ${error ? "login-error" : ""}`}
                placeholder={LABELS.EMAIL}
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            <div className="login-form-group">
              <div className="login-input-group">
                <input
                  type={passwordVisible ? "text" : "password"}
                  className={`login-form-control ${error ? "login-error" : ""}`}
                  placeholder={LABELS.PASSWORD}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
                <button
                  type="button"
                  className="login-btn-outline-secondary"
                  onClick={() => setPasswordVisible(!passwordVisible)}
                >
                  {passwordVisible ? <FaEye /> : <FaEyeSlash />}
                </button>
              </div>
            </div>
            {error && <div className="login-text-danger mb-3">{error}</div>}
            <button type="submit" className="login-btn-primary">
              {LABELS.LOGIN_BUTTON}
            </button>
          </form>
          <div className="login-link mt-3">
            <button
              onClick={() => setShowForgotPasswordModal(true)}
              className="forgot-password-link"
            >
              {LABELS.FORGOT_PASSWORD}
            </button>
          </div>
          <div className="login-link mt-3">
            <span>{LABELS.DONT_HAVE_ACCOUNT} </span>
            <Link to={PATHS.SIGN_UP}>{LABELS.SIGN_UP}</Link>
          </div>
        </div>
      </div>
      <ForgotPasswordModal
        showModal={showForgotPasswordModal}
        closeModal={() => setShowForgotPasswordModal(false)}
      />
      <Footer />
    </div>
  );
};

export default Login;
