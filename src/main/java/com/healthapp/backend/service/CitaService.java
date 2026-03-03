package com.healthapp.backend.service;

import com.healthapp.backend.dto.CitaMedicaDTO;
import com.healthapp.backend.model.CitaMedica;
import com.healthapp.backend.model.EstadoCita;
import com.healthapp.backend.model.Paciente;
import com.healthapp.backend.model.Usuario;
import com.healthapp.backend.repository.CitaMedicaRepository;
import com.healthapp.backend.repository.PacienteRepository;
import com.healthapp.backend.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaService {

    private final CitaMedicaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;

    public CitaService(CitaMedicaRepository citaRepository, PacienteRepository pacienteRepository, UsuarioRepository usuarioRepository) {
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Paciente obtenerPacienteAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return pacienteRepository.findByUsuarioId(usuario.getId()).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }

    private CitaMedicaDTO mapearADto(CitaMedica cita) {
        return CitaMedicaDTO.builder()
                .id(cita.getId())
                .fecha(cita.getFecha())
                .hora(cita.getHora())
                .especialidad(cita.getEspecialidad())
                .descripcion(cita.getDescripcion())
                .estado(cita.getEstado().name())
                .nombrePaciente(cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellidos())
                .nombreMedico(cita.getMedico() != null ? cita.getMedico().getNombre() : "Sin asignar")
                .build();
    }

    public List<CitaMedicaDTO> listarCitasPaciente(LocalDate fecha, EstadoCita estado) {
        Paciente paciente = obtenerPacienteAutenticado();

        return paciente.getCitas().stream()
                .filter(cita -> fecha == null || cita.getFecha().equals(fecha))
                .filter(cita -> estado == null || cita.getEstado().equals(estado))
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    public CitaMedicaDTO obtenerCitaPorId(Long id) {
        CitaMedica cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        Paciente pacienteActual = obtenerPacienteAutenticado();
        if (!cita.getPaciente().getId().equals(pacienteActual.getId())) {
            throw new RuntimeException("No tienes permiso para ver esta cita");
        }

        return mapearADto(cita);
    }

    @Transactional
    public void crearCita(CitaMedicaDTO citaDTO) {
        Paciente paciente = obtenerPacienteAutenticado();

        CitaMedica nuevaCita = new CitaMedica();
        nuevaCita.setFecha(citaDTO.getFecha());
        nuevaCita.setHora(citaDTO.getHora());
        nuevaCita.setEspecialidad(citaDTO.getEspecialidad());
        nuevaCita.setDescripcion(citaDTO.getDescripcion());
        nuevaCita.setEstado(EstadoCita.PROGRAMADA);
        nuevaCita.setPaciente(paciente);


        citaRepository.save(nuevaCita);
    }

    @Transactional
    public void actualizarCita(Long id, CitaMedicaDTO citaDTO) {
        CitaMedica cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        Paciente pacienteActual = obtenerPacienteAutenticado();
        if (!cita.getPaciente().getId().equals(pacienteActual.getId())) {
            throw new RuntimeException("No tienes permiso para editar esta cita");
        }

        cita.setFecha(citaDTO.getFecha());
        cita.setHora(citaDTO.getHora());
        cita.setEspecialidad(citaDTO.getEspecialidad());
        cita.setDescripcion(citaDTO.getDescripcion());

        citaRepository.save(cita);
    }

    @Transactional
    public void cancelarCita(Long id) {
        CitaMedica cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        Paciente pacienteActual = obtenerPacienteAutenticado();
        if (!cita.getPaciente().getId().equals(pacienteActual.getId())) {
            throw new RuntimeException("No tienes permiso para cancelar esta cita");
        }

        cita.setEstado(EstadoCita.CANCELADA);
        citaRepository.save(cita);
    }

    public List<CitaMedicaDTO> listarCitasMedico() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario medico = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        return citaRepository.findByMedicoId(medico.getId()).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    public List<CitaMedicaDTO> listarCitasDisponibles() {

        return citaRepository.findByMedicoIsNullAndEstado(EstadoCita.PROGRAMADA).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void aceptarCita(Long idCita) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario medico = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        CitaMedica cita = citaRepository.findById(idCita).orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (cita.getMedico() != null) {
            throw new RuntimeException("Esta cita ya ha sido asignada a otro médico.");
        }

        cita.setMedico(medico);
        citaRepository.save(cita);
    }

    @Transactional
    public void cambiarEstadoCita(Long idCita, EstadoCita nuevoEstado) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario medico = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        CitaMedica cita = citaRepository.findById(idCita).orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (cita.getMedico() == null || !cita.getMedico().getId().equals(medico.getId())) {
            throw new RuntimeException("No tienes permiso para modificar una cita que no es tuya.");
        }

        cita.setEstado(nuevoEstado);
        citaRepository.save(cita);
    }
}