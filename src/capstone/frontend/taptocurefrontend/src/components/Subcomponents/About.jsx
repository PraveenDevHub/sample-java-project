import React from 'react'
import aboutus from '../../assests/aboutus.jpg'

function About() {
    return (

        <div id='about'>
            <h2>About Us</h2>
            <div className='about-section'>
                <div className="about-image">
                    <img src={aboutus} alt="" />
                </div>
                <div className="about-text">
                    <p>TapToCure is a seamless online doctor appointment booking platform designed to connect patients with healthcare professionals quickly and efficiently. With a user-friendly interface, patients can browse a diverse network of specialists, view detailed doctor profiles, and select from available time slots to book appointments instantly. The platform prioritizes patient convenience and security, offering personalized health management features and secure data storage. TapToCure’s commitment to quality ensures that only verified and experienced doctors are listed. TapToCure aims to make healthcare accessible, enabling users to manage their appointments, access healthcare, and take charge of their well-being with ease.</p>
                </div>
            </div>
        </div>
    )
}

export default About