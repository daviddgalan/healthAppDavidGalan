package com.healthapp.backend.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponseDTO {
    private String token;
}