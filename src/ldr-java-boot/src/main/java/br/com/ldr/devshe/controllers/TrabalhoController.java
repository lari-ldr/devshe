package br.com.ldr.devshe.controllers;

import br.com.ldr.devshe.dto.TrabalhoRequestDTO;
import br.com.ldr.devshe.dto.TrabalhoResponseDTO;
import br.com.ldr.devshe.exceptions.NotFoundError;
import br.com.ldr.devshe.services.TrabalhoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Secured({"ROLE_ADMIN", "ROLE_USER"})
@RestController
@RequestMapping("/api/v1/trabalho")
@Validated
public class TrabalhoController extends BaseController {

    private final TrabalhoService trabalhoService;

    @Autowired
    public TrabalhoController(TrabalhoService trabalhoService) {
        this.trabalhoService = trabalhoService;
    }

    @PostMapping
    public ResponseEntity<TrabalhoResponseDTO> criar(
            @RequestBody @Valid TrabalhoRequestDTO dto) throws NotFoundError {
        TrabalhoResponseDTO response = trabalhoService.criarTrabalho(getUsuario(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/meus")
    public ResponseEntity<List<TrabalhoResponseDTO>> buscarReviewPorEmpresa() {
        return ResponseEntity.ok(trabalhoService.listarTrabalhosPorUsuario(getUsuario()));
    }
}
