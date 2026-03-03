package com.healthapp.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "citas_medicas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private LocalTime hora;
    private String especialidad;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Usuario medico;
}
