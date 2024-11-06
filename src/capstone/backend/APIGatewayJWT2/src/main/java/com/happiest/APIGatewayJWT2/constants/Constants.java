package com.happiest.APIGatewayJWT2.constants;

public class Constants {
    // Messages
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String EMAIL_NOT_FOUND = "The email address you entered does not exist. Please check and try again";
    public static final String EMAIL_NOT_VERIFIED = "Email not verified";
    public static final String INVALID_EMAIL_OR_PASSWORD = "Invalid email or password";
    public static final String INVALID_OR_EXPIRED_TOKEN = "Invalid or expired token";
    public static final String USER_NOT_FOUND = "User not found";

    // Expiration times
    public static final long VERIFICATION_TOKEN_EXPIRY = 3600000; // 1 hour in milliseconds

    // URLs
    public static final String LOGIN_REDIRECT_URL = "http://localhost:3000/login";
}

