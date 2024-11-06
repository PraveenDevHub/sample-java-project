package com.happiest.APIGatewayJWT2.service;

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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static com.happiest.APIGatewayJWT2.constants.Constants.*;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepo repo;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private VerificationTokenRepo tokenRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users user, Object roleSpecificData) {
        Optional<Users> existingUser = repo.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException(EMAIL_ALREADY_EXISTS);
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(false);
        Users savedUser = repo.save(user);

        ObjectMapper mapper = new ObjectMapper();
        if (user.getRole() == Users.Role.Doctor) {
            Doctors doctor = mapper.convertValue(roleSpecificData, Doctors.class);
            doctor.setUser(savedUser);
            doctorRepo.save(doctor);
        } else if (user.getRole() == Users.Role.Patient) {
            Patients patient = mapper.convertValue(roleSpecificData, Patients.class);
            patient.setUser(savedUser);
            patientRepo.save(patient);
        }

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(savedUser);
        verificationToken.setExpiryDate(new Date(System.currentTimeMillis() + VERIFICATION_TOKEN_EXPIRY));
        tokenRepo.save(verificationToken);

        emailService.sendVerificationEmail(savedUser.getEmail(), token);
        return savedUser;
    }

    public Map<String, Object> verify(Users user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );

        if (authentication.isAuthenticated()) {
            Users authenticatedUser = repo.findByEmail(user.getEmail())
                    .orElseThrow(() -> new EmailNotFoundException(EMAIL_NOT_FOUND));
            if (!authenticatedUser.isEnabled()) {
                throw new EmailNotVerifiedException(EMAIL_NOT_VERIFIED);
            }
            String token = jwtService.generateToken(user.getEmail());
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("role", authenticatedUser.getRole().name());
            response.put("isVerified", authenticatedUser.isEnabled());
            response.put("userId", authenticatedUser.getUserId());
            response.put("name", authenticatedUser.getName());

            if (authenticatedUser.getRole() == Users.Role.Doctor) {
                Doctors doctor = doctorRepo.findByUser(authenticatedUser);
                response.put("doctorId", doctor.getDoctorId());
            } else if (authenticatedUser.getRole() == Users.Role.Patient) {
                Patients patient = patientRepo.findByUser(authenticatedUser);
                response.put("patientId", patient.getPatientId());
            }

            return response;
        } else {
            throw new IllegalArgumentException(INVALID_EMAIL_OR_PASSWORD);
        }
    }

    public boolean verifyEmail(String token) {
        VerificationToken verificationToken = tokenRepo.findByToken(token);
        if (verificationToken == null || new Date().after(verificationToken.getExpiryDate())) {
            throw new InvalidTokenException(INVALID_OR_EXPIRED_TOKEN);
        }

        Users user = verificationToken.getUser();
        user.setEnabled(true);
        repo.save(user);
        tokenRepo.delete(verificationToken);

        return true;
    }

    public void saveVerificationToken(VerificationToken verificationToken) {
        tokenRepo.save(verificationToken);
    }

    public void save(Users user) {
        repo.save(user);
    }

    public void generatePasswordResetToken(String email) {
        Optional<Users> userOptional = repo.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new EmailNotFoundException(USER_NOT_FOUND);
        }
        Users user = userOptional.get();
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(new Date(System.currentTimeMillis() + VERIFICATION_TOKEN_EXPIRY));
        tokenRepo.save(verificationToken);
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    public void resetPassword(String token, String newPassword) {
        VerificationToken verificationToken = tokenRepo.findByToken(token);
        if (verificationToken == null || new Date().after(verificationToken.getExpiryDate())) {
            throw new InvalidTokenException(INVALID_OR_EXPIRED_TOKEN);
        }
        Users user = verificationToken.getUser();
        user.setPassword(encoder.encode(newPassword));
        repo.save(user);
        tokenRepo.delete(verificationToken);
    }

}
