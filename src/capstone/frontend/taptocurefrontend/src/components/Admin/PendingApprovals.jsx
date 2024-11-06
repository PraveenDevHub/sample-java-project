import React, { useState, useEffect } from "react";
import axios from "axios";
import "../../css/pendingapprovals.css"; // Separate CSS
import {
  FaUserMd,
  FaClipboardCheck,
  FaChartBar,
  FaTachometerAlt,
  FaUser,
  FaRegTimesCircle,
} from "react-icons/fa";
import { useSelector } from "react-redux";
import Navbar from "../Subcomponents/Navbar";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Button,
} from "@mui/material";
import { API_BASE_URL, APPROVE_DOCTOR_URL, REJECT_DOCTOR_URL, MESSAGES, LABELS, PATHS } from "./constants";

const PendingApprovals = () => {
  const [doctors, setDoctors] = useState([]);
  const [selectedDoctor, setSelectedDoctor] = useState(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [dialogMessage, setDialogMessage] = useState("");
  const [dialogAction, setDialogAction] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState('');

  const token = useSelector((state) => state.auth.token);

  useEffect(() => {
    axios
      .get(`${API_BASE_URL}/admin/pending-approvals`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => setDoctors(response.data))
      .catch((error) => {
        console.error("Error fetching pending approvals:", error);
        if (error.response) {
          console.error('Error response:', error.response.data);
          setErrorMessage(error.response?.data);
        } else if (error.request) {
          console.error('Error request:', error.request);
          setErrorMessage(MESSAGES.NO_RESPONSE);
          toast.error(MESSAGES.NO_RESPONSE, {
            position: "bottom-left",
            autoClose: 5000,
          });
        } else {
          console.error('Error:', error.message);
          setErrorMessage(MESSAGES.UNEXPECTED_ERROR);
          toast.error(MESSAGES.UNEXPECTED_ERROR, {
            position: "bottom-left",
            autoClose: 5000,
          });
        }
      })
      .finally(() => {
        setIsLoading(false);
      });
  }, [token]);

  const handleSelectDoctor = (doctor) => {
    setSelectedDoctor(doctor);
  };

  const handleApprove = (doctorId) => {
    setDialogMessage(MESSAGES.APPROVE_CONFIRM);
    setDialogAction(() => () => {
      setIsLoading(true);
      axios
        .put(
          `${APPROVE_DOCTOR_URL}/${doctorId}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then(() => {
          toast.success(MESSAGES.APPROVE_SUCCESS, {
            position: "bottom-left",
            autoClose: 5000,
          });
          setDoctors(doctors.filter((doc) => doc.doctorId !== doctorId));
        })
        .catch(() => {
          toast.error(MESSAGES.APPROVE_FAILURE, {
            position: "bottom-left",
            autoClose: 5000,
          });
        })
        .finally(() => {
          setIsLoading(false);
        });
    });
    setOpenDialog(true);
  };

  const handleReject = (doctorId) => {
    setDialogMessage(MESSAGES.REJECT_CONFIRM);
    setDialogAction(() => () => {
      setIsLoading(true);
      axios
        .put(
          `${REJECT_DOCTOR_URL}/${doctorId}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then(() => {
          toast.success(MESSAGES.REJECT_SUCCESS, {
            position: "bottom-left",
            autoClose: 5000,
          });
          setDoctors(doctors.filter((doc) => doc.doctorId !== doctorId));
        })
        .catch(() => {
          toast.error(MESSAGES.REJECT_FAILURE, {
            position: "bottom-left",
            autoClose: 5000,
          });
        })
        .finally(() => {
          setIsLoading(false);
        });
    });
    setOpenDialog(true);
  };

  const closePopup = () => {
    setSelectedDoctor(null);
  };

  return (
    <div>
      <Navbar />
      <div className="pending-approvals">
        <div className="pending-approvals__sidebar">
          <h2>{LABELS.ADMIN_PANEL}</h2>
          <ul>
            <li onClick={() => (window.location.href = PATHS.ADMIN_HOME)}>
              <FaTachometerAlt /> {LABELS.DASHBOARD}
            </li>
            <li onClick={() => (window.location.href = PATHS.PATIENTS)}>
              <FaUser /> {LABELS.PATIENTS}
            </li>
            <li onClick={() => (window.location.href = PATHS.DOCTORS)}>
              <FaUserMd /> {LABELS.DOCTORS}
            </li>
            <li onClick={() => (window.location.href = PATHS.PENDING_APPROVALS)}>
              <FaClipboardCheck /> {LABELS.PENDING_APPROVALS}
            </li>
            <li onClick={() => (window.location.href = PATHS.STATISTICS)}>
              <FaChartBar /> {LABELS.STATISTICS}
            </li>
          </ul>
        </div>

        <div className="pending-approvals__content">
          <h1>{LABELS.PENDING_DOCTOR_APPROVALS}</h1>
          {errorMessage}
          {isLoading ? (
            <div className="dot-container">
              <div className="dot-falling"></div>
            </div>
          ) : (
            <div className="pending-approvals__cards-container">
              {doctors.map((doctor) => (
                <div
                  key={doctor.doctorId}
                  className="pending-approvals__doctor-card"
                  onClick={() => handleSelectDoctor(doctor)}
                >
                  <h2>{doctor.user.name}</h2>
                  <p>{LABELS.SPECIALIZATION}: {doctor.specialization}</p>
                  <p>{LABELS.LOCATION}: {doctor.city},{doctor.state}</p>
                  <button className="approve-button">{LABELS.VIEW_DETAILS}</button>
                </div>
              ))}
            </div>
          )}

          {selectedDoctor && (
            <div className="pending-approvals__overlay">
              <div className="pending-approvals__doctor-popup">
                <button
                  className="pending-approvals__back-button"
                  onClick={closePopup}
                >
                  {LABELS.BACK}<FaRegTimesCircle/>
                </button>
                <h2>{selectedDoctor.user.name}</h2>
                <p>{LABELS.EMAIL}: {selectedDoctor.user.email}</p>
                <p>{LABELS.SPECIALIZATION}: {selectedDoctor.specialization}</p>
                <p>{LABELS.MEDICAL_LICENSE}: {selectedDoctor.medicalLicenseNumber}</p>
                <p>{LABELS.YEARS_OF_EXPERIENCE}: {selectedDoctor.yearsOfExperience}</p>
                <p>{LABELS.HOSPITAL}: {selectedDoctor.hospitalName}</p>
                <p>{LABELS.LOCATION}: {selectedDoctor.city},{selectedDoctor.state}</p>
                
                <div className="pending-approvals__buttons">
                  <button
                    className="approve-button"
                    onClick={() => handleApprove(selectedDoctor.doctorId)}
                  >
                    {LABELS.APPROVE}
                  </button>
                  <button
                    className="reject-button"
                    onClick={() => handleReject(selectedDoctor.doctorId)}
                  >
                    {LABELS.REJECT}
                  </button>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
      <ToastContainer />
      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>{LABELS.CONFIRM_ACTION}</DialogTitle>
        <DialogContent>
          <DialogContentText>{dialogMessage}</DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => {
              setOpenDialog(false);
              closePopup();
            }}
            color="primary"
          >
            {LABELS.NO}
          </Button>
          <Button
            onClick={() => {
              setOpenDialog(false);
              closePopup();
              dialogAction();
            }}
            color="primary"
          >
            {LABELS.YES}
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default PendingApprovals;
