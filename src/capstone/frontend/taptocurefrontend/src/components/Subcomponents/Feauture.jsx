import React from 'react';
import FeatureBox from './FeatureBox';

function Feature() {
    return (
        <div id='features'>
            <h2>Features</h2>
            <div className="a-container">
                <FeatureBox
                    icon="🔍"
                    title="Quick Specialist Search"
                    description="Find doctors by specialty, location, hospital in seconds."
                />
                <FeatureBox
                    icon="🔄"
                    title="Easy Rescheduling & Cancellations"
                    description="Flexibly adjust or cancel appointments with just a few clicks."
                />
                <FeatureBox
                    icon="🕒"
                    title="Timely Appointment Reminders"
                    description="Receive automatic notifications to ensure you never miss an appointment."
                />
            </div>
        </div>
    );
}

export default Feature;
