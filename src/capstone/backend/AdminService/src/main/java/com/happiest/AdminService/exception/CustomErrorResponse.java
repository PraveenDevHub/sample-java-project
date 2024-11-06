package com.happiest.AdminService.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponse {
    private String status; // Custom status (e.g., "fail")
    private String message; // Error message
}
