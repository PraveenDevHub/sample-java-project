import React from 'react';

function FeatureBox({ icon, title, description }) {
    return (
        <div className='a-box'>
            <div className='a-b-img'>
                <span className='feature-icon'>{icon}</span>
            </div>
            <div className="s-b-text">
                <h3>{title}</h3>
                <p>{description}</p>
            </div>
        </div>
    );
}

export default FeatureBox;