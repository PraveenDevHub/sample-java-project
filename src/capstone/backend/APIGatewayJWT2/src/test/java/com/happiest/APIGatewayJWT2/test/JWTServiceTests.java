package com.happiest.APIGatewayJWT2.test;

import com.happiest.APIGatewayJWT2.constants.JWTConstants;
import com.happiest.APIGatewayJWT2.exception.TokenException;
import com.happiest.APIGatewayJWT2.service.JWTService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JWTServiceTests {

    @InjectMocks
    private JWTService jwtService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        setJwtSecret(jwtService, "01234567890123456789012345678901"); // Set a valid test secret (32 bytes)
    }

    private void setJwtSecret(JWTService jwtService, String secret) throws Exception {
        Field field = JWTService.class.getDeclaredField("jwtSecret");
        field.setAccessible(true);
        field.set(jwtService, secret);
    }

    @Test
    public void testGenerateToken_Success() {
        String token = jwtService.generateToken("testUser");
        assertNotNull(token);
    }

    @Test
    public void testExtractUserName_Success() throws JOSEException, ParseException {
        String token = jwtService.generateToken("testUser");

        try (MockedStatic<SignedJWT> mockedSignedJWT = mockStatic(SignedJWT.class)) {
            SignedJWT signedJWT = mock(SignedJWT.class);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject("testUser").build();

            mockedSignedJWT.when(() -> SignedJWT.parse(token)).thenReturn(signedJWT);
            when(signedJWT.verify(any())).thenReturn(true);
            when(signedJWT.getJWTClaimsSet()).thenReturn(claimsSet);

            String userName = jwtService.extractUserName(token);
            assertEquals("testUser", userName);
        }
    }

    @Test
    public void testValidateToken_Success() throws JOSEException, ParseException {
        String token = jwtService.generateToken("testUser");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        try (MockedStatic<SignedJWT> mockedSignedJWT = mockStatic(SignedJWT.class)) {
            SignedJWT signedJWT = mock(SignedJWT.class);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject("testUser")
                    .expirationTime(new Date(System.currentTimeMillis() + 10000))
                    .build();

            mockedSignedJWT.when(() -> SignedJWT.parse(token)).thenReturn(signedJWT);
            when(signedJWT.verify(any())).thenReturn(true);
            when(signedJWT.getJWTClaimsSet()).thenReturn(claimsSet);

            boolean isValid = jwtService.validateToken(token, userDetails);
            assertTrue(isValid);
        }
    }

    @Test
    public void testValidateToken_Expired() throws JOSEException, ParseException {
        String token = jwtService.generateToken("testUser");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        try (MockedStatic<SignedJWT> mockedSignedJWT = mockStatic(SignedJWT.class)) {
            SignedJWT signedJWT = mock(SignedJWT.class);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject("testUser")
                    .expirationTime(new Date(System.currentTimeMillis() - 10000))
                    .build();

            mockedSignedJWT.when(() -> SignedJWT.parse(token)).thenReturn(signedJWT);
            when(signedJWT.verify(any())).thenReturn(true);
            when(signedJWT.getJWTClaimsSet()).thenReturn(claimsSet);

            boolean isValid = jwtService.validateToken(token, userDetails);
            assertFalse(isValid);
        }
    }

    @Test
    public void testExtractAllClaims_InvalidToken() {
        String invalidToken = "invalidToken";

        assertThrows(TokenException.class, () -> jwtService.extractAllClaims(invalidToken));
    }
}
