package br.com.ldr.devshe.controllers;

import br.com.ldr.devshe.domain.Usuario;
import br.com.ldr.devshe.security.JWTAuthentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {

    public Usuario getUsuario() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() instanceof JWTAuthentication) {
            JWTAuthentication auth = (JWTAuthentication) context.getAuthentication();
            return auth != null ? auth.getUsuario() : null;
        }
        return null;
    }
}
