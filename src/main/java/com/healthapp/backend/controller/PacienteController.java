package com.healthapp.backend.controller;

import com.healthapp.backend.dto.CitaMedicaDTO;
import com.healthapp.backend.dto.PacienteDTO;
import com.healthapp.backend.model.EstadoCita;
import com.healthapp.backend.service.CitaService;
import com.healthapp.backend.service.PacienteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/paciente")
@PreAuthorize("hasRole('ROLE_PACIENTE')")
public class PacienteController {

    private final PacienteService pacienteService;
    private final CitaService citaService;

    public PacienteController(PacienteService pacienteService, CitaService citaService) {
        this.pacienteService = pacienteService;
        this.citaService = citaService;
    }

    // === GESTIÓN DEL PERFIL ===

    @GetMapping("/perfil")
    public String verPerfil(Model model) {
        model.addAttribute("paciente", pacienteService.obtenerPerfil());
        return "paciente/perfil";
    }

    @GetMapping("/editar")
    public String mostrarFormularioEdicion(Model model) {
        model.addAttribute("pacienteDTO", pacienteService.obtenerPerfil());
        return "paciente/editar-perfil";
    }

    @PostMapping("/editar")
    public String guardarPerfil(@ModelAttribute("pacienteDTO") PacienteDTO pacienteDTO) {
        pacienteService.actualizarPerfil(pacienteDTO);
        return "redirect:/paciente/perfil?actualizado";
    }

    // === GESTIÓN DE CITAS ===

    @GetMapping("/citas")
    public String listarCitas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) EstadoCita estado,
            Model model) {
        List<CitaMedicaDTO> citas = citaService.listarCitasPaciente(fecha, estado);
        model.addAttribute("citas", citas);
        return "paciente/citas";
    }

    @GetMapping("/citas/nueva")
    public String mostrarFormularioNuevaCita(Model model) {
        model.addAttribute("citaDTO", new CitaMedicaDTO());
        return "paciente/nueva-cita";
    }

    @PostMapping("/citas/nueva")
    public String guardarNuevaCita(@ModelAttribute("citaDTO") CitaMedicaDTO citaDTO) {
        citaService.crearCita(citaDTO);
        return "redirect:/paciente/citas?creada";
    }

    @GetMapping("/citas/{id}")
    public String verDetalleCita(@PathVariable Long id, Model model) {
        model.addAttribute("cita", citaService.obtenerCitaPorId(id));
        return "paciente/detalle-cita";
    }

    @GetMapping("/citas/editar/{id}")
    public String mostrarFormularioEdicionCita(@PathVariable Long id, Model model) {
        model.addAttribute("citaDTO", citaService.obtenerCitaPorId(id));
        return "paciente/editar-cita";
    }

    @PostMapping("/citas/editar/{id}")
    public String actualizarCita(@PathVariable Long id, @ModelAttribute("citaDTO") CitaMedicaDTO citaDTO) {
        citaService.actualizarCita(id, citaDTO);
        return "redirect:/paciente/citas?actualizada";
    }

    @PostMapping("/citas/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id) {
        citaService.cancelarCita(id);
        return "redirect:/paciente/citas?cancelada";
    }
}