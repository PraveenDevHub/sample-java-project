import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { clearAuth } from "../../authSlice";
import "../../css/navbar.css";
import logo from "../../assests/logo2.png";

function Navbar() {
  const name = useSelector((state) => state.auth.name);
  const [nav, setNav] = useState(false);
  const [showPopup, setShowPopup] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const getInitials = (name) => {
    const names = name.split(" ");
    const initials = names.map((n) => n[0]).join("");
    return initials.toUpperCase();
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      handleLogout();
    }, 15 * 60 * 1000);

    return () => clearTimeout(timer);
  }, []);

  const handleLogout = () => {
    dispatch(clearAuth());
    navigate("/");
  };

  const changeBackground = () => {
    if (window.scrollY >= 50) {
      setNav(true);
    } else {
      setNav(false);
    }
  };
  window.addEventListener("scroll", changeBackground);

  const renderMenuItems = () => {
    switch (location.pathname) {
      case "/landing":
        return (
          <>
            <li>
              <a href="/">Home</a>
            </li>
            <li>
              <a href="#features">Features</a>
            </li>
            <li>
              <a href="#about">About</a>
            </li>
            <li>
              <a href="/login">Login/Sign Up</a>
            </li>
          </>
        );
      case "/login":
      case "/signup":
      case "/reset-password":
        return (
          <>
            <li>
              <a href="/">Home</a>
            </li>
            <li>
              <a href="/login">Login/Sign Up</a>
            </li>
          </>
        );
      case "/doctorhome":
        return (
          <>
            <li>
              <a href="/doctorhome">My Dashboard</a>
            </li>
            <li>
              <a href="/schedule">Schedule</a>
            </li>
            <li>
              <a href="/default-schedule">Default Schedule</a>
            </li>
            <li>
              <a href="/myschedule">My Schedule</a>
            </li>
            <li>
              <div
                className="profile-img-container"
                onClick={() => setShowPopup(!showPopup)}
              >
                <div className="profile-img">{getInitials(name)}</div>
              </div>
              {showPopup && (
                <div className="profile-popup">
                  <ul>
                  <li onClick={() => navigate("/doctor-appointmenthistory")}>
                  Appointment History
                  </li>
                    <li onClick={() => navigate("/editdoctorprofile")}>
                      View Profile
                    </li>
                    <li onClick={handleLogout}>Logout</li>
                  </ul>
                </div>
              )}
            </li>
          </>
        );
      case "/schedule":
        return (
          <>
            <li>
              <a href="/doctorhome">My Dashboard</a>
            </li>
            <li>
              <a href="/default-schedule">Default Schedule</a>
            </li>
            <li>
              <a href="/myschedule">My Schedule</a>
            </li>
            <li>
              <div
                className="profile-img-container"
                onClick={() => setShowPopup(!showPopup)}
              >
                <div className="profile-img">{getInitials(name)}</div>
              </div>
              {showPopup && (
                <div className="profile-popup">
                  <ul>
                  <li onClick={() => navigate("/doctor-appointmenthistory")}>
                  Appointment History
                  </li>
                    <li onClick={() => navigate("/editdoctorprofile")}>
                      View Profile
                    </li>
                    <li onClick={handleLogout}>Logout</li>
                  </ul>
                </div>
              )}
            </li>
          </>
        );
      case "/default-schedule":
        return (
          <>
            <li>
              <a href="/doctorhome">My Dashboard</a>
            </li>
            <li>
              <a href="/schedule">Schedule</a>
            </li>
            <li>
              <a href="/myschedule">My Schedule</a>
            </li>
            <li>
              <div
                className="profile-img-container"
                onClick={() => setShowPopup(!showPopup)}
              >
                <div className="profile-img">{getInitials(name)}</div>
              </div>
              {showPopup && (
                <div className="profile-popup">
                  <ul>
                  <li onClick={() => navigate("/doctor-appointmenthistory")}>
                  Appointment History
                  </li>
                    <li onClick={() => navigate("/editdoctorprofile")}>
                      View Profile
                    </li>
                    <li onClick={handleLogout}>Logout</li>
                  </ul>
                </div>
              )}
            </li>
          </>
        );
      case "/editdoctorprofile":
        return (
          <>
          <li>
              <a href="/doctorhome">My Dashboard</a>
            </li>
            <li>
              <a href="/schedule">Schedule</a>
            </li>
            <li>
              <a href="/default-schedule">Default Schedule</a>
            </li>
            <li>
              <a href="/myschedule">My Schedule</a>
            </li>
            <li>
              <div
                className="profile-img-container"
                onClick={() => setShowPopup(!showPopup)}
              >
                <div className="profile-img">{getInitials(name)}</div>
              </div>
              {showPopup && (
                <div className="profile-popup">
                  <ul>
                  <li onClick={() => navigate("/doctor-appointmenthistory")}>
                  Appointment History
                  </li>
                    <li onClick={handleLogout}>Logout</li>
                  </ul>
                </div>
              )}
            </li>
          </>
        );
      case "/myschedule":
        return (
          <>
            <li>
              <a href="/doctorhome">My Dashboard</a>
            </li>
            <li>
              <a href="/schedule">Schedule</a>
            </li>
            <li>
              <a href="/default-schedule">Default Schedule</a>
            </li>
            <li>
              <div
                className="profile-img-container"
                onClick={() => setShowPopup(!showPopup)}
              >
                <div className="profile-img">{getInitials(name)}</div>
              </div>
              {showPopup && (
                <div className="profile-popup">
                  <ul>
                  <li onClick={() => navigate("/doctor-appointmenthistory")}>
                  Appointment History
                  </li>
                  <li onClick={() => navigate("/editdoctorprofile")}>
                      View Profile
                    </li>
                    <li onClick={handleLogout}>Logout</li>
                  </ul>
                </div>
              )}
            </li>
          </>
        );
        case "/doctor-appointmenthistory":
          return (
            <>
              <li>
                <a href="/doctorhome">My Dashboard</a>
              </li>
              <li>
                <a href="/schedule">Schedule</a>
              </li>
              <li>
                <a href="/default-schedule">Default Schedule</a>
              </li>
              <li>
              <a href="/myschedule">My Schedule</a>
              </li>
              <li>
                <div
                  className="profile-img-container"
                  onClick={() => setShowPopup(!showPopup)}
                >
                  <div className="profile-img">{getInitials(name)}</div>
                </div>
                {showPopup && (
                  <div className="profile-popup">
                    <ul>
                    <li onClick={() => navigate("/editdoctorprofile")}>
                        View Profile
                      </li>
                      <li onClick={handleLogout}>Logout</li>
                    </ul>
                  </div>
                )}
              </li>
            </>
          );
      case "/editpatientprofile":
      case "/bookappointment":
        return (
          <>
            <li>
              <a href="/patienthome">My Dashboard</a>
            </li>
            <li>
              <a href="/patientappointments">Upcoming Appointments</a>
            </li>
            <li>
              <a href="/patient-appointmenthistory">Appointment History</a>
            </li>
            <li>
              <div
                className="profile-img-container"
                onClick={() => setShowPopup(!showPopup)}
              >
                <div className="profile-img">{getInitials(name)}</div>
              </div>
              {showPopup && (
                <div className="profile-popup">
                  <ul>
                    <li onClick={handleLogout}>
                      <i className="fas fa-sign-out-alt"></i> Logout
                    </li>
                  </ul>
                </div>
              )}
            </li>
          </>
        );
      case "/patienthome":
        return (
          <>
            <li>
              <a href="/patienthome">My Dashboard</a>
            </li>
            <li>
              <a href="/patientappointments">Upcoming Appointments</a>
            </li>
            <li>
              <a href="/patient-appointmenthistory">Appointment History</a>
            </li>
            <li>
              <div
                className="profile-img-container"
                onClick={() => setShowPopup(!showPopup)}
              >
                <div className="profile-img">{getInitials(name)}</div>
              </div>
              {showPopup && (
                <div className="profile-popup">
                  <ul>
                    <li onClick={() => navigate("/editpatientprofile")}>
                      View Profile
                    </li>
                    <li onClick={handleLogout}>
                      <i className="fas fa-sign-out-alt"></i> Logout
                    </li>
                  </ul>
                </div>
              )}
            </li>
          </>
        );
      case "/patient-appointmenthistory":
        return (
          <>
            <li>
              <a href="/patienthome">My Dashboard</a>
            </li>
            <li>
              <a href="/patientappointments">Upcoming Appointments</a>
            </li>
            <li>
              <div
                className="profile-img-container"
                onClick={() => setShowPopup(!showPopup)}
              >
                <div className="profile-img">{getInitials(name)}</div>
              </div>
              {showPopup && (
                <div className="profile-popup">
                  <ul>
                    <li onClick={() => navigate("/editpatientprofile")}>
                      View Profile
                    </li>
                    <li onClick={handleLogout}>
                      <i className="fas fa-sign-out-alt"></i> Logout
                    </li>
                  </ul>
                </div>
              )}
            </li>
          </>
        );
      case "/reset-password":
        return (
          <>
          </>
        );
      case "/patientappointments":
        return (
          <>
            <li>
              <a href="/patienthome">My Dashboard</a>
            </li>
            <li>
              <a href="/patient-appointmenthistory">Appointment History</a>
            </li>
            <li>
              <div
                className="profile-img-container"
                onClick={() => setShowPopup(!showPopup)}
              >
                <div className="profile-img">{getInitials(name)}</div>
              </div>
              {showPopup && (
                <div className="profile-popup">
                  <ul>
                    <li onClick={() => navigate("/editpatientprofile")}>
                      View Profile
                    </li>
                    <li onClick={handleLogout}>
                      <i className="fas fa-sign-out-alt"></i> Logout
                    </li>
                  </ul>
                </div>
              )}
            </li>
          </>
        );
      case "/adminhome":
      case "/patients":
      case "/doctors":
      case "/pending-approvals":
      case "/statistics":
        return (
          <>
            <li>
              <a href="/adminhome">My Dashboard</a>
            </li>
            <li>
              <div
                className="profile-img-container"
                onClick={() => setShowPopup(!showPopup)}
              >
                <div className="profile-img">{getInitials(name)}</div>
              </div>
              {showPopup && (
                <div className="profile-popup">
                  <ul>
                    <li onClick={handleLogout}>
                      <i className="fas fa-sign-out-alt"></i> Logout
                    </li>
                  </ul>
                </div>
              )}
            </li>
          </>
        );
      default:
        return (
          <>
            <li>
              <a href="/">Home</a>
            </li>
            <li>
              <a href="#features">Features</a>
            </li>
            <li>
              <a href="#about">About</a>
            </li>
            <li>
              <a href="/login">Login/Sign Up</a>
            </li>
          </>
        );
    }
  };

  return (
    <nav className={nav ? "nav active" : "nav"}>
      <div className="logo-container">
        <a href="#main" className="logo">
          <img src={logo} alt="TapToCure Logo" className="logo-image" />
        </a>
        {(location.pathname === "/doctorhome" ||
          location.pathname === "/patienthome") && (
            <span className="welcome-text">Welcome, {name}</span>
          )}
      </div>
      <input type="checkbox" className="menu-btn" id="menu-btn" />
      <label className="menu-icon" htmlFor="menu-btn">
        <span className="nav-icon"></span>
      </label>
      <ul className="menu">{renderMenuItems()}</ul>
    </nav>
  );
}

export default Navbar;
