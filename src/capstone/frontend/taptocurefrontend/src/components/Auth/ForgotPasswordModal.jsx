import React, { useState, useEffect } from "react";
import axios from "../../axiosConfig";
import { FaCheckCircle, FaExclamationCircle } from "react-icons/fa";
import "../../css/ForgotPassword.css";

const ForgotPasswordModal = ({ showModal, closeModal }) => {
  const [email, setEmail] = useState("");
  const [toast, setToast] = useState({ message: "", type: "" });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (toast.message) {
      const timer = setTimeout(() => {
        setToast({ message: "", type: "" });
      }, 5000); // 5000 milliseconds = 5 seconds

      return () => clearTimeout(timer); // Cleanup the timer on component unmount or when toast changes
    }
  }, [toast]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await axios.post("/forgot-password", { email });
      setToast({ message: response.data, type: "success" });
    } catch (error) {
      setToast({
        message: "Error sending password reset email",
        type: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  if (!showModal) {
    return null;
  }

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <button className="modal-close" onClick={closeModal}>
          ×
        </button>
        <h2 className="forgot-password-title">Forgot Password</h2>
        <form onSubmit={handleSubmit} className="forgot-password-form">
          <div className="forgot-password-form-group">
            <input
              type="email"
              className={`forgot-password-form-control ${
                toast.type === "error" ? "forgot-password-error" : ""
              }`}
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Email"
              required
            />
          </div>
          <button
            type="submit"
            className="forgot-password-btn-primary"
            disabled={loading}
          >
            {loading ? "Sending..." : "Send Reset Link"}
          </button>
        </form>
        {loading && (
          <div className="forgot-password-loading">
            <span></span>
            <span></span>
            <span></span>
          </div>
        )}
        {toast.message && (
          <div
            className={`forgot-password-toast ${
              toast.type === "success"
                ? "forgot-password-toast-success"
                : "forgot-password-toast-error"
            }`}
          >
            <span className="forgot-password-toast-icon">
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
    </div>
  );
};

export default ForgotPasswordModal;
