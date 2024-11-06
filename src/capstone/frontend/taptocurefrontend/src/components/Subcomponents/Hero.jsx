import React from "react";
import Navbar from "./Navbar";
import '../../css/Hero.css';

function Hero() {
  return (
    <div id="main" className="hero-section">
      <Navbar />
      <div className="hero-content text-center">
        <h1>
          <span>Care Just a Click Away</span> Hassle-Free Healthcare Starts Here
        </h1>
        <a href="/patienthome" className="btn btn-primary btn-lg">
          Book Appointment   <i className="fa-solid fa-arrow-right"></i>
        </a>
      </div>
    </div>
  );
}

export default Hero;
