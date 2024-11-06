package com.happiest.APIGatewayJWT2.test;

import com.happiest.APIGatewayJWT2.model.UserPrincipal;
import com.happiest.APIGatewayJWT2.model.Users;
import com.happiest.APIGatewayJWT2.repository.UserRepo;
import com.happiest.APIGatewayJWT2.service.MyUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MyUserDetailsServiceTests {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserPrincipal userDetails = (UserPrincipal) myUserDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        verify(userRepo, times(1)).findByEmail("test@example.com");
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> myUserDetailsService.loadUserByUsername("test@example.com"));
        verify(userRepo, times(1)).findByEmail("test@example.com");
    }
}
