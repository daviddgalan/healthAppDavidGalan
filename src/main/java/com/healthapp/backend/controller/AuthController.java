package com.healthapp.backend.controller;

import com.healthapp.backend.dto.LoginDTO;
import com.healthapp.backend.dto.RegistroDTO;
import com.healthapp.backend.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute("loginDTO") LoginDTO loginDTO, HttpServletResponse response, Model model) {
        try {
            String token = authService.login(loginDTO);

            Cookie jwtCookie = new Cookie("JWT", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(10 * 60 * 60);

            response.addCookie(jwtCookie);

            String rol = authService.obtenerRolUsuario(loginDTO.getEmail());

            if ("ROLE_MEDICO".equals(rol)) {
                return "redirect:/medico/citas";
            } else {
                return "redirect:/paciente/citas";
            }

        } catch (Exception e) {
            model.addAttribute("error", "Email o contraseña incorrectos");
            return "login";
        }
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("registroDTO", new RegistroDTO());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute("registroDTO") RegistroDTO registroDTO, Model model) {
        try {
            authService.registrarPaciente(registroDTO);
            return "redirect:/login?registroExitoso";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }


}
