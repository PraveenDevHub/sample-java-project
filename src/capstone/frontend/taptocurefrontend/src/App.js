import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import Landing from "./components/Landing";
import DoctorHome from "./components/Doctor/DoctorHome";
import Login from "./components/Auth/Login";
import Signup from "./components/Auth/Signup";
import PatientHome from "./components/Patient/PatientHome";
import ResetPassword from "./components/Auth/ResetPassword";
import Schedule from "./components/Doctor/Schedule";
import "./index.css";
import Feature from "./components/Subcomponents/Feauture";
import PublicRoute from "./components/Auth/PublicRoute";
import DoctorProfile from "./components/Doctor/DoctorProfile";
import ProtectedRoute from "./components/Auth/ProtectedRoute";
import Patients from "./components/Admin/Patients";
import Doctors from "./components/Admin/Doctors";
import PatientAppointments from "./components/Patient/PatientAppointments";
import AppointmentHistory from "./components/Patient/AppointmentHistory";
import PendingApprovals from "./components/Admin/PendingApprovals";
import Statistics from "./components/Admin/Statistics";
import AdminDashboard from "./components/Admin/AdminDashboard";
import BookAppointment from "./components/Patient/BookAppointment";
import DefaultSchedule from "./components/Doctor/DefaultSchedule";
import EditPatientProfile from "./components/Patient/EditPatientProfile";
import DoctorAppointmentHistory from "./components/Doctor/DoctorAppointmentHistory";
import MySchedule from "./components/Doctor/MySchedule";

function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/" element={<Landing />} />
          <Route element={<PublicRoute />}>
            <Route path="/signup" element={<Signup />} />
            <Route path="/login" element={<Login />} />
            <Route path="/reset-password" element={<ResetPassword />} />
          </Route>
          <Route element={<ProtectedRoute allowedRoles={["Doctor"]} />}>
            <Route path="/doctorhome" element={<DoctorHome />} />
            <Route path="/editdoctorprofile" element={<DoctorProfile />} />
            <Route path="/schedule" element={<Schedule />} />
            <Route path="/default-schedule" element={<DefaultSchedule />} />
            <Route
              path="/doctor-appointmenthistory"
              element={<DoctorAppointmentHistory />}
            />
            <Route path="/myschedule" element={<MySchedule />} />
          </Route>
          <Route element={<ProtectedRoute allowedRoles={["Patient"]} />}>
            <Route path="/patienthome" element={<PatientHome />} />
            <Route
              path="/patientappointments"
              element={<PatientAppointments />}
            />
            <Route
              path="/patient-appointmenthistory"
              element={<AppointmentHistory />}
            />
            <Route path="/bookappointment" element={<BookAppointment />} />
            <Route
              path="/editpatientprofile"
              element={<EditPatientProfile />}
            />
          </Route>
          <Route element={<ProtectedRoute allowedRoles={["Admin"]} />}>
            <Route path="/patients" element={<Patients />} />
            <Route path="/doctors" element={<Doctors />} />
            <Route path="/pending-approvals" element={<PendingApprovals />} />
            <Route path="/statistics" element={<Statistics />} />
            <Route path="/adminhome" element={<AdminDashboard />} />
          </Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App;
