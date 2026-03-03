package com.healthapp.backend.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private String rol;
}