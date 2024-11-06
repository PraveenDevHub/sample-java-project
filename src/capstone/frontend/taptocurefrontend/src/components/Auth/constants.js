// src/components/Auth/constants.js
export const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;
export const LOGIN_URL = process.env.REACT_APP_LOGIN_URL;
export const RESET_PASSWORD_URL = process.env.REACT_APP_RESET_PASSWORD_URL;
export const REGISTER_URL = process.env.REACT_APP_REGISTER_URL;

export const MESSAGES = {
  VERIFY_EMAIL: "Please verify your email before logging in.",
  LOGIN_SUCCESS: "Login successful!",
  INVALID_CREDENTIALS: "Invalid email or password.",
  EMAIL_NOT_VERIFIED: "Email not verified.",
  EMAIL_NOT_EXIST: "The email address you entered does not exist. Please check and try again.",
  LOGIN_FAILED: "Login failed. Please check your email and password.",
  PASSWORD_RESET_SUCCESS: "Password reset successful! Please log in with your new password.",
  PASSWORDS_DO_NOT_MATCH: "Passwords do not match.",
  ERROR_RESETTING_PASSWORD: "Error resetting password.",
  REGISTRATION_SUCCESS: "Registration successful! Please check your email for verification.",
  EMAIL_ALREADY_EXISTS: "Email already exists. Please sign up with another email.",
  REGISTRATION_FAILED: "Registration failed. Please try again.",
  ENTER_VALID_DETAILS: "Enter valid details.",
  PASSWORD_CRITERIA_NOT_MET: "Password does not meet the criteria.",
  NAME_VALIDATION: "Name should only contain letters and spaces.",
  EMAIL_VALIDATION: "Email is not valid.",
  YEARS_OF_EXPERIENCE_VALIDATION: "Years of Experience should be a positive number with up to 2 digits.",
  AGE_VALIDATION: "Age should be a positive number with up to 2 digits.",
  CONTACT_NUMBER_VALIDATION: "Contact number should only contain digits.",
  CONTACT_NUMBER_LENGTH_VALIDATION: "Contact number should be a 10-digit number.",
  HOSPITAL_NAME_VALIDATION: "Hospital name should only contain letters and spaces.",
  ERROR_CHECKING_VERIFICATION_STATUS: "Error checking verification status:",
  ERROR_DURING_REGISTRATION: "Error during registration:",
};

export const LABELS = {
  LOGIN: "Login",
  EMAIL: "Email*",
  PASSWORD: "Password*",
  FORGOT_PASSWORD: "Forgot Password?",
  SIGN_UP: "Sign up",
  DONT_HAVE_ACCOUNT: "Don't have an account?",
  LOGIN_TITLE: "Login",
  LOGIN_BUTTON: "Login",
  RESET_PASSWORD: "Reset Password",
  NEW_PASSWORD: "New Password*",
  CONFIRM_PASSWORD: "Confirm Password*",
  PASSWORD_VALIDATION: {
    LENGTH: "Password must be over 8 characters.",
    CASE: "Must contain both lower and upper case letters.",
    NUMBER: "Must contain at least one number (0-9) or symbol.",
  },
  NAME: "Name*",
  ROLE: "Role*",
  SELECT_ROLE: "Select Role",
  PATIENT: "Patient",
  DOCTOR: "Doctor",
  MEDICAL_LICENSE_NUMBER: "Medical License Number*",
  SPECIALIZATION: "Specialization*",
  YEARS_OF_EXPERIENCE: "Years of Experience*",
  HOSPITAL_NAME: "Hospital Name*",
  STATE: "State*",
  CITY: "City*",
  AGE: "Age*",
  GENDER: "Gender*",
  CONTACT_NUMBER: "Contact Number*",
  ALREADY_HAVE_ACCOUNT: "Already have an account?",
  SIGNING_UP: "Signing Up...",
  SIGN_UP_BUTTON: "Sign Up",
};

export const SPECIALIZATIONS = [
  "General",
  "Cardiology",
  "Dermatology",
  "Neurology",
  "Pediatrics",
  "Oncology",
  "Psychiatry",
  "Gynecology",
  "Dentistry",
  "Ophthalmology",
];

export const GENDERS = [
  "Male",
  "Female",
  "Other",
];

export const PATHS = {
  DOCTOR_HOME: "/doctorhome",
  PATIENT_HOME: "/patienthome",
  ADMIN_HOME: "/adminhome",
  SIGN_UP: "/signup",
  LOGIN: "/login",
};
