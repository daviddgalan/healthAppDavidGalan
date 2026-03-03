package com.healthapp.backend.dto;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class RegistroDTO {
    private String nombre;
    private String apellidos;
    private String email;
    private String contrasena;
    private LocalDate fechaNacimiento;
    private String telefono;
}