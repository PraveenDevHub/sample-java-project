package com.happiest.APIGatewayJWT2.test;

import com.happiest.APIGatewayJWT2.filter.JwtFilter;
import com.happiest.APIGatewayJWT2.service.JWTService;
import com.happiest.APIGatewayJWT2.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtFilterTests {

    @Mock
    private JWTService jwtService;

    @Mock
    private ApplicationContext context;

    @Mock
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtFilter jwtFilter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoFilterInternal_ValidToken() throws Exception {
        String token = "valid-token";
        String username = "testUser";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUserName(token)).thenReturn(username);
        when(context.getBean(MyUserDetailsService.class)).thenReturn(myUserDetailsService);

        UserDetails userDetails = mock(UserDetails.class);
        when(myUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);

        Method doFilterInternal = JwtFilter.class.getDeclaredMethod("doFilterInternal", HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
        doFilterInternal.setAccessible(true);
        doFilterInternal.invoke(jwtFilter, request, response, filterChain);

        verify(jwtService, times(1)).extractUserName(token);
        verify(jwtService, times(1)).validateToken(token, userDetails);
        verify(context, times(1)).getBean(MyUserDetailsService.class);
        verify(myUserDetailsService, times(1)).loadUserByUsername(username);
        verify(filterChain, times(1)).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternal_InvalidToken() throws Exception {
        String token = "invalid-token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUserName(token)).thenReturn(null);

        Method doFilterInternal = JwtFilter.class.getDeclaredMethod("doFilterInternal", HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
        doFilterInternal.setAccessible(true);
        doFilterInternal.invoke(jwtFilter, request, response, filterChain);

        verify(jwtService, times(1)).extractUserName(token);
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternal_NoToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        Method doFilterInternal = JwtFilter.class.getDeclaredMethod("doFilterInternal", HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
        doFilterInternal.setAccessible(true);
        doFilterInternal.invoke(jwtFilter, request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
