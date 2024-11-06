package com.happiest.APIGatewayJWT2.constants;

public class EmailConstants {
    // Email Subjects
    public static final String EMAIL_VERIFICATION_SUBJECT = "Email Verification";
    public static final String PASSWORD_RESET_SUBJECT = "Password Reset Request";

    // Email Body Texts
    public static final String EMAIL_VERIFICATION_TEXT = "<p>Please click the button below to verify your email:</p>"
            + "<a href=\"%s\" style=\"padding: 10px; background-color: #28a745; color: white; text-decoration: none; border-radius: 5px;\">Confirm Email</a>";
    public static final String PASSWORD_RESET_TEXT = "<p>Please click the link below to reset your password:</p>"
            + "<a href=\"%s\" style=\"padding: 10px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">Reset Password</a>";

    // URLs
    public static final String VERIFICATION_URL = "http://localhost:8091/verify?token=";
    public static final String PASSWORD_RESET_URL = "http://localhost:3000/reset-password?token=";

    // Error Messages
    public static final String EMAIL_SENDING_FAILURE = "Failed to send email to: ";
    public static final String EMAIL_VERIFICATION_FAILURE = "Failed to send verification email to: ";
    public static final String PASSWORD_RESET_FAILURE = "Failed to send password reset email to: ";
}
