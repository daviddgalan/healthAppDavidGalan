package com.healthapp.backend.repository;

import com.healthapp.backend.model.CitaMedica;
import com.healthapp.backend.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {

    List<CitaMedica> findByPacienteIdAndFechaAndEstado(Long pacienteId, LocalDate fecha, EstadoCita estado);

    List<CitaMedica> findByMedicoId(Long medicoId);
    List<CitaMedica> findByMedicoIsNullAndEstado(EstadoCita estado);
}
