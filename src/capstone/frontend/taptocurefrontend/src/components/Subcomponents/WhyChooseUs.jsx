import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMobileAlt, faUserMd, faCalendarCheck, faShieldAlt, faHeart, faCertificate } from '@fortawesome/free-solid-svg-icons';
import '../../css/whychooseus.css'

const WhyChooseUs = () => {
    return (
        <div className="why-choose-us-container">
            <h2 className="why-choose-us-title">Why Choose TapToCure?</h2>

            <div className="why-choose-us-content">
                <div className="why-choose-us-item">
                    <FontAwesomeIcon icon={faMobileAlt} className="why-choose-us-icon" data-testid="fa-mobile-alt" />
                    <h3>User-Friendly Experience</h3>
                    <p>
                        Our mobile-first design ensures booking appointments, checking doctor availability,
                        and managing your health records is as easy as a few taps on your phone.
                    </p>
                </div>

                <div className="why-choose-us-item">
                    <FontAwesomeIcon icon={faUserMd} className="why-choose-us-icon" data-testid="fa-user-md" />
                    <h3>Wide Range of Specialists</h3>
                    <p>
                        TapToCure connects you with a diverse network of healthcare professionals,
                        offering access to general physicians and specialized experts.
                    </p>
                </div>

                <div className="why-choose-us-item">
                    <FontAwesomeIcon icon={faCalendarCheck} className="why-choose-us-icon" data-testid="fa-calendar-check" />
                    <h3>Instant Appointment Booking</h3>
                    <p>
                        Book appointments in real-time and get instant confirmation. Say goodbye to waiting on hold!
                    </p>
                </div>

                <div className="why-choose-us-item">
                    <FontAwesomeIcon icon={faShieldAlt} className="why-choose-us-icon" data-testid="fa-shield-alt" />
                    <h3>Secure Patient Data</h3>
                    <p>
                        We protect your health information with top-tier encryption, ensuring all your data is private and safe.
                    </p>
                </div>

                <div className="why-choose-us-item">
                    <FontAwesomeIcon icon={faHeart} className="why-choose-us-icon" data-testid="fa-heart" />
                    <h3>Personalized Health Management</h3>
                    <p>
                        Manage your appointments, get reminders, and store prescriptions in one place with ease.
                    </p>
                </div>

                <div className="why-choose-us-item">
                    <FontAwesomeIcon icon={faCertificate} className="why-choose-us-icon" data-testid="fa-certificate" />
                    <h3>Trusted by Healthcare Professionals</h3>
                    <p>
                        Every doctor on our platform is certified and experienced, providing you with high-quality care.
                    </p>
                </div>
            </div>
        </div>
    );
};

export default WhyChooseUs;
