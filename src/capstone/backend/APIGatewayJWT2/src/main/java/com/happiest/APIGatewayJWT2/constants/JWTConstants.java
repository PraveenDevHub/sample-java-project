package com.happiest.APIGatewayJWT2.constants;

public class JWTConstants {
    // JWT Errors
    public static final String TOKEN_GENERATION_ERROR = "Error generating token";
    public static final String TOKEN_EXTRACTION_ERROR = "Error extracting claims";
    public static final String INVALID_TOKEN_SIGNATURE = "Invalid token signature";

    // Token Expiration
    public static final long TOKEN_EXPIRATION_TIME_MS = 60 * 60 * 3000; // Adjust as necessary for your token expiration logic
}
