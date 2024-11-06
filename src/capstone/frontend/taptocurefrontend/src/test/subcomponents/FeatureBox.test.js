import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import FeatureBox from '../../components/Subcomponents/FeatureBox';

describe('FeatureBox', () => {
  const icon = <svg data-testid="icon" />;
  const title = 'Feature Title';
  const description = 'This is a feature description.';

  test('renders FeatureBox component', () => {
    render(<FeatureBox icon={icon} title={title} description={description} />);

    // Check if the icon is rendered
    expect(screen.getByTestId('icon')).toBeInTheDocument();

    // Check if the title is rendered
    expect(screen.getByText(title)).toBeInTheDocument();

    // Check if the description is rendered
    expect(screen.getByText(description)).toBeInTheDocument();
  });

  test('renders with correct class names', () => {
    render(<FeatureBox icon={icon} title={title} description={description} />);

    // Check if the container has the correct class name
    expect(screen.getByText(title).closest('.a-box')).toBeInTheDocument();

    // Check if the icon container has the correct class name
    expect(screen.getByTestId('icon').closest('.a-b-img')).toBeInTheDocument();

    // Check if the text container has the correct class name
    expect(screen.getByText(title).closest('.s-b-text')).toBeInTheDocument();
  });
});
