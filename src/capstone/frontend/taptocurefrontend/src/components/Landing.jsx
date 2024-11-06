import React, { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import Hero from './Subcomponents/Hero';
import Feature from './Subcomponents/Feauture';
import About from './Subcomponents/About';
import Footer from './Subcomponents/Footer';
import WhyChooseUs from './Subcomponents/WhyChooseUs';
import '../css/Hero.css';

function Landing() {
  const location = useLocation();

  useEffect(() => {
    if (location.hash) {
      const element = document.querySelector(location.hash);
      if (element) {
        element.scrollIntoView({ behavior: 'smooth' });
      }
    }
  }, [location]);

  return (
    <div>
      <Hero />
      <Feature id="features" />
      <WhyChooseUs />
      <About id="about" />
      <Footer />
    </div>
  );
}

export default Landing;
