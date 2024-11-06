import React, { useState, useEffect } from "react";
import axios from "axios";
import { Bar, Line } from "react-chartjs-2"; // Import both Bar and Line components
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from "chart.js"; // Import necessary Chart.js components
import "../../css/statistics.css"; // Import the CSS file
import {
  FaUserMd,
  FaClipboardCheck,
  FaChartBar,
  FaTachometerAlt,
  FaUser,
} from "react-icons/fa";
import { useSelector } from "react-redux";
import Navbar from "../Subcomponents/Navbar";
import { DASHBOARD_COUNTS_URL, APPOINTMENT_STATS_URL, MESSAGES, LABELS, PATHS } from "./constants";

// Register the necessary components
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

const Statistics = () => {
  const [counts, setCounts] = useState({
    users: 0,
    patients: 0,
    doctors: 0,
    appointments: 0,
    approvals: 0,
  });

  const [appointmentStats, setAppointmentStats] = useState({
    daily: {},
    monthly: {},
  });

  const token = useSelector((state) => state.auth.token);
  console.log(token);

  useEffect(() => {
    axios
      .get(DASHBOARD_COUNTS_URL, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        setCounts(response.data);
      })
      .catch((error) => console.error(MESSAGES.FETCH_STATS_ERROR, error));

    axios.get(APPOINTMENT_STATS_URL, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
      .then((response) => {
        console.log('Appointment Statistics:', response.data); // Debugging line
        setAppointmentStats(response.data);
      })
      .catch(error => console.error(MESSAGES.FETCH_APPOINTMENT_STATS_ERROR, error));
  }, [token]);

  const formatData = (data) => {
    if (!data || Object.keys(data).length === 0) {
      return {
        labels: [],
        datasets: [],
      };
    }

    return {
      labels: Object.keys(data),
      datasets: [
        {
          label: LABELS.NUMBER_OF_APPOINTMENTS,
          data: Object.values(data),
          fill: false,
          backgroundColor: 'rgba(75,192,192,0.4)',
          borderColor: 'rgba(75,192,192,1)',
          tension: 0.4, // Smooth the line
        },
      ],
    };
  };

  const data = {
    labels: [
      LABELS.USERS,
      LABELS.PATIENTS,
      LABELS.DOCTORS,
      LABELS.APPOINTMENTS,
      LABELS.PENDING_APPROVALS,
    ],
    datasets: [
      {
        label: LABELS.COUNT_BAR,
        data: [
          counts.users,
          counts.patients,
          counts.doctors,
          counts.appointments,
          counts.approvals,
        ],
        backgroundColor: [
          "#36A2EB",
          "#FF6384",
          "#FFCE56",
          "#4BC0C0",
          "#FF9F40",
        ],
        hoverBackgroundColor: [
          "#36A2EB",
          "#FF6384",
          "#FFCE56",
          "#4BC0C0",
          "#FF9F40",
        ],
      },
    ],
  };

  return (
    <div>
      <Navbar />
      <div className="statistics">
        <div className="statistics__sidebar">
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
        <div className="statistics__content">
          <h1 className="statisticsHeader">{LABELS.ADMIN_DASHBOARD_STATS}</h1>
          <div className="statisticsChartSection">
            <Bar data={data} className="statisticsCanvas" />
          </div>
          <div className="statisticsChartSection">
            <h2>{LABELS.DAILY_APPOINTMENTS}</h2>
            <Line data={formatData(appointmentStats.daily)} className="statisticsCanvas" />
            <h2>{LABELS.MONTHLY_APPOINTMENTS}</h2>
            <Line data={formatData(appointmentStats.monthly)} className="statisticsCanvas" />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Statistics;
