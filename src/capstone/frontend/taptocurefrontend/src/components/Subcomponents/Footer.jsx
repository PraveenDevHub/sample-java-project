import React from 'react';
import '../../css/Footer.css';
import { Link } from 'react-router-dom';

function Footer() {
    return (
        <footer className="footer">
            <div className="container">
                <div className="footer-content">
                    <div className="footer-section about">
                        <h2>TapToCure</h2>
                        <p>TapToCure is your trusted platform for booking appointments with the best doctors in the industry. Experience seamless scheduling and expert care in just a few clicks.</p>
                    </div>

                    <div className="footer-section links">
                        <h2>Quick Links</h2>
                        <ul>
                            <li><Link to="/">Home</Link></li>
                            <li><Link to="/#features">Features</Link></li>
                            <li><Link to="/#about">About</Link></li>
                        </ul>
                    </div>

                    <div className="footer-section contact">
                        <h2>Contact Us</h2>
                        <ul>
                            <li>Email: support@taptocure.com</li>
                            <li>Phone: +123-456-7890</li>
                            <li>Address: Electronic City, Bengaluru</li>
                        </ul>
                    </div>

                    <div className="footer-section social">
                        <h2>Follow Us</h2>
                        <ul className="social-icons">
                            <li><a href="#"><i className="fab fa-facebook-f"></i></a></li>
                            <li><a href="#"><i className="fab fa-twitter"></i></a></li>
                            <li><a href="#"><i className="fab fa-linkedin-in"></i></a></li>
                            <li><a href="#"><i className="fab fa-instagram"></i></a></li>
                        </ul>
                    </div>
                </div>
                <div className="footer-bottom">
                    <p>© {new Date().getFullYear()} TapToCure. All rights reserved.</p>
                </div>
            </div>
        </footer>
    );
}

export default Footer;
