package com.happiest.PatientService.test;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.core.env.Environment;

import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest

@ActiveProfiles("test")

public class ProfileTest {

    @Autowired

    private Environment env;

    @Test

    public void testProfileIsActive() {

        String[] activeProfiles = env.getActiveProfiles();

        assertTrue(List.of(activeProfiles).contains("test"), "Test profile is not active");

    }

}


