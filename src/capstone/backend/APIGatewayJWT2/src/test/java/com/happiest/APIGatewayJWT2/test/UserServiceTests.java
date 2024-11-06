package com.happiest.APIGatewayJWT2.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.happiest.APIGatewayJWT2.exception.*;
import com.happiest.APIGatewayJWT2.model.Doctors;
import com.happiest.APIGatewayJWT2.model.Patients;
import com.happiest.APIGatewayJWT2.model.Users;
import com.happiest.APIGatewayJWT2.model.VerificationToken;
import com.happiest.APIGatewayJWT2.repository.DoctorRepo;
import com.happiest.APIGatewayJWT2.repository.PatientRepo;
import com.happiest.APIGatewayJWT2.repository.UserRepo;
import com.happiest.APIGatewayJWT2.repository.VerificationTokenRepo;
import com.happiest.APIGatewayJWT2.service.EmailService;
import com.happiest.APIGatewayJWT2.service.JWTService;
import com.happiest.APIGatewayJWT2.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @Mock
    private UserRepo userRepo;

    @Mock
    private DoctorRepo doctorRepo;

    @Mock
    private PatientRepo patientRepo;

    @Mock
    private VerificationTokenRepo tokenRepo;

    @Mock
    private EmailService emailService;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private UserService userService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(Users.Role.Patient);

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepo.save(any(Users.class))).thenReturn(user);

        Users registeredUser = userService.register(user, new Patients());

        assertNotNull(registeredUser);
        verify(userRepo, times(1)).save(any(Users.class));
        verify(emailService, times(1)).sendVerificationEmail(anyString(), anyString());
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        Users user = new Users();
        user.setEmail("test@example.com");

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.register(user, new Patients()));
    }

    @Test
    public void testVerifyUser_Success() {
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setEnabled(true);
        user.setRole(Users.Role.Patient); // Ensure role is set

        Patients patient = new Patients();
        patient.setUser(user);
        patient.setPatientId(1); // Set a valid patient ID

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(patientRepo.findByUser(user)).thenReturn(patient); // Mock patientRepo
        when(jwtService.generateToken(user.getEmail())).thenReturn("jwt-token");

        Map<String, Object> response = userService.verify(user);

        assertNotNull(response);
        assertEquals("jwt-token", response.get("token"));
        assertEquals("Patient", response.get("role"));
        assertEquals(true, response.get("isVerified"));
        assertEquals(user.getUserId(), response.get("userId"));
        assertEquals(user.getName(), response.get("name"));
        assertEquals(patient.getPatientId(), response.get("patientId"));
    }

    @Test
    public void testVerifyUser_EmailNotFound() {
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> userService.verify(user));
    }

    @Test
    public void testVerifyEmail_Success() {
        Users user = new Users();
        user.setEmail("test@example.com");
        VerificationToken token = new VerificationToken();
        token.setToken("valid-token");
        token.setUser(user);
        token.setExpiryDate(new Date(System.currentTimeMillis() + 10000));

        when(tokenRepo.findByToken("valid-token")).thenReturn(token);

        boolean result = userService.verifyEmail("valid-token");

        assertTrue(result);
        verify(userRepo, times(1)).save(user);
        verify(tokenRepo, times(1)).delete(token);
    }

    @Test
    public void testVerifyEmail_InvalidToken() {
        when(tokenRepo.findByToken("invalid-token")).thenReturn(null);

        assertThrows(InvalidTokenException.class, () -> userService.verifyEmail("invalid-token"));
    }

    @Test
    public void testGeneratePasswordResetToken_Success() {
        Users user = new Users();
        user.setEmail("test@example.com");

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        userService.generatePasswordResetToken(user.getEmail());

        verify(tokenRepo, times(1)).save(any(VerificationToken.class));
        verify(emailService, times(1)).sendPasswordResetEmail(anyString(), anyString());
    }

    @Test
    public void testGeneratePasswordResetToken_EmailNotFound() {
        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> userService.generatePasswordResetToken("test@example.com"));
    }

    @Test
    public void testResetPassword_Success() {
        Users user = new Users();
        user.setEmail("test@example.com");
        VerificationToken token = new VerificationToken();
        token.setToken("valid-token");
        token.setUser(user);
        token.setExpiryDate(new Date(System.currentTimeMillis() + 10000));

        when(tokenRepo.findByToken("valid-token")).thenReturn(token);

        userService.resetPassword("valid-token", "new-password");

        verify(userRepo, times(1)).save(user);
        verify(tokenRepo, times(1)).delete(token);
    }

    @Test
    public void testResetPassword_InvalidToken() {
        when(tokenRepo.findByToken("invalid-token")).thenReturn(null);

        assertThrows(InvalidTokenException.class, () -> userService.resetPassword("invalid-token", "new-password"));
    }
}
