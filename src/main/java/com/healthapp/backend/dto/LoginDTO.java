package com.healthapp.backend.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class LoginDTO {
    private String email;
    private String contrasena;
}