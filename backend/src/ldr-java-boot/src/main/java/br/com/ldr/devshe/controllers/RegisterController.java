package br.com.ldr.devshe.controllers;

import br.com.ldr.devshe.dto.Autorizacao;
import br.com.ldr.devshe.dto.UsuarioDTO;
import br.com.ldr.devshe.dto.UsuarioRequest;
import br.com.ldr.devshe.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RegisterController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/api/v1/register")
    public ResponseEntity<Autorizacao> create(@RequestBody @Valid UsuarioRequest request) {
        Autorizacao auth = usuarioService.createAndAuth(request);
        if (auth != null) {
            return new ResponseEntity<>(auth, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
