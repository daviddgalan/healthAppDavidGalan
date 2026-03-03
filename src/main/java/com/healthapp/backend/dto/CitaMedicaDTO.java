package com.healthapp.backend.dto;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CitaMedicaDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String especialidad;
    private String descripcion;
    private String estado;
    private String nombrePaciente;
    private String nombreMedico;
}