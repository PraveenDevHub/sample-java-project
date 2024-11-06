package com.happiest.APIGatewayJWT2.constants;

public class UserControllerConstants {
    // Response Messages
    public static final String USER_REGISTERED_SUCCESSFULLY_MSG = "User registered successfully. Please check your email for verification.";
    public static final String PASSWORD_RESET_EMAIL_SENT = "Password reset email sent";
    public static final String PASSWORD_RESET_SUCCESSFUL_MSG = "Password reset successful";
    public static final String USER_NOT_FOUND_MSG = "User not found";
    public static final String INVALID_OR_EXPIRED_TOKEN_MSG = "Invalid or expired token";
    public static final String ERROR_REDIRECTING_TO_LOGIN_MSG = "An error occurred while redirecting to the login page.";
    public static final String SCHEDULE_CONFLICT_MSG = "Schedule already exists for the selected date and time.";
    public static final String SCHEDULE_UPDATED_SUCCESSFULLY_MSG = "Schedule updated successfully";
    public static final String DEFAULT_SCHEDULE_SAVED_SUCCESSFULLY_MSG = "Default schedule saved successfully";

    // URL
    public static final String LOGIN_URL = "http://localhost:3000/login";

    // Token Expiry
    public static final long TOKEN_EXPIRY_DURATION = 3600000; // 1 hour in milliseconds
}
