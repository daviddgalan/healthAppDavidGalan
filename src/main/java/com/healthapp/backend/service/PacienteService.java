package com.healthapp.backend.service;

import com.healthapp.backend.dto.PacienteDTO;
import com.healthapp.backend.model.Paciente;
import com.healthapp.backend.model.Usuario;
import com.healthapp.backend.repository.PacienteRepository;
import com.healthapp.backend.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;

    public PacienteService(PacienteRepository pacienteRepository, UsuarioRepository usuarioRepository) {
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Paciente obtenerPacienteAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return pacienteRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }

    public PacienteDTO obtenerPerfil() {
        Paciente paciente = obtenerPacienteAutenticado();

        return PacienteDTO.builder()
                .id(paciente.getId())
                .nombre(paciente.getNombre())
                .apellidos(paciente.getApellidos())
                .fechaNacimiento(paciente.getFechaNacimiento())
                .telefono(paciente.getTelefono())
                .email(paciente.getUsuario().getEmail())
                .build();
    }

    @Transactional
    public void actualizarPerfil(PacienteDTO dto) {
        Paciente paciente = obtenerPacienteAutenticado();

        paciente.setNombre(dto.getNombre());
        paciente.setApellidos(dto.getApellidos());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setTelefono(dto.getTelefono());
        pacienteRepository.save(paciente);

        Usuario usuario = paciente.getUsuario();
        usuario.setNombre(dto.getNombre());
        usuarioRepository.save(usuario);
    }
}