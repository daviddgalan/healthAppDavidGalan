package com.healthapp.backend.service;

import com.healthapp.backend.dto.LoginDTO;
import com.healthapp.backend.dto.RegistroDTO;
import com.healthapp.backend.model.Paciente;
import com.healthapp.backend.model.Rol;
import com.healthapp.backend.model.Usuario;
import com.healthapp.backend.repository.PacienteRepository;
import com.healthapp.backend.repository.UsuarioRepository;
import com.healthapp.backend.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository, PacienteRepository pacienteRepository,
                       PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getContrasena())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtil.generateToken(userDetails);
    }

    @Transactional
    public void registrarPaciente(RegistroDTO registroDTO) {
        if (usuarioRepository.findByEmail(registroDTO.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(registroDTO.getNombre());
        nuevoUsuario.setEmail(registroDTO.getEmail());
        nuevoUsuario.setContrasena(passwordEncoder.encode(registroDTO.getContrasena()));
        nuevoUsuario.setRol(Rol.ROLE_PACIENTE);

        usuarioRepository.save(nuevoUsuario);

        Paciente nuevoPaciente = new Paciente();
        nuevoPaciente.setNombre(registroDTO.getNombre());
        nuevoPaciente.setApellidos(registroDTO.getApellidos());
        nuevoPaciente.setFechaNacimiento(registroDTO.getFechaNacimiento());
        nuevoPaciente.setTelefono(registroDTO.getTelefono());
        nuevoPaciente.setUsuario(nuevoUsuario);

        pacienteRepository.save(nuevoPaciente);
    }

    public String obtenerRolUsuario(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getRol().name();
    }
}