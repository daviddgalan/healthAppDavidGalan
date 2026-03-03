package com.healthapp.backend.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Atrapa cualquier excepción no controlada en toda la aplicación
    @ExceptionHandler(Exception.class)
    public String manejarExcepcionGlobal(Exception ex, Model model) {
        // Pasamos el mensaje de error real a la vista para informar al usuario
        model.addAttribute("mensajeError", ex.getMessage());
        return "error"; // Redirige a templates/error.html
    }
}