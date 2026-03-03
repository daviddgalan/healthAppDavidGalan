package com.healthapp.backend.controller;

import com.healthapp.backend.model.EstadoCita;
import com.healthapp.backend.service.CitaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medico")
@PreAuthorize("hasRole('ROLE_MEDICO')")
public class MedicoController {

    private final CitaService citaService;

    public MedicoController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping("/citas")
    public String verPanelMedico(Model model) {
        model.addAttribute("citasDisponibles", citaService.listarCitasDisponibles());
        model.addAttribute("citasAsignadas", citaService.listarCitasMedico());
        return "medico/citas";
    }

    @PostMapping("/citas/aceptar/{id}")
    public String aceptarCita(@PathVariable Long id) {
        citaService.aceptarCita(id);
        return "redirect:/medico/citas?aceptada";
    }

    @PostMapping("/citas/estado/{id}")
    public String cambiarEstado(@PathVariable Long id, @RequestParam EstadoCita estado) {
        citaService.cambiarEstadoCita(id, estado);
        return "redirect:/medico/citas?actualizada";
    }
}