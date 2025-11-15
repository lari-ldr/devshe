package br.com.ldr.devshe.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.com.ldr.devshe.dto.UsuarioDTO;
import br.com.ldr.devshe.dto.UsuarioRequest;
import br.com.ldr.devshe.exceptions.NotFoundError;
import br.com.ldr.devshe.services.UsuarioService;

@Secured({"ROLE_ADMIN"})
@RestController
@RequestMapping("/api/v1/user")
@Validated
public class UsuarioController {
	
	private final UsuarioService usuarioService;
	
	@Autowired
	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@PutMapping("/{uuid}")
	public ResponseEntity<UsuarioDTO> update(@PathVariable("uuid") String uuid, @Valid @RequestBody UsuarioRequest request) throws NotFoundError {
		return new ResponseEntity<>(new UsuarioDTO(usuarioService.update(uuid, request)), HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<UsuarioDTO> create(@RequestBody @Valid UsuarioRequest request) {
		return new ResponseEntity<>(new UsuarioDTO(usuarioService.create(request)), HttpStatus.CREATED);
	}

}