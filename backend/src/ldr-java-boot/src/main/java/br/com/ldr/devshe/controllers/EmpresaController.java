package br.com.ldr.devshe.controllers;

import br.com.ldr.devshe.domain.Empresa;
import br.com.ldr.devshe.dto.EmpresaRequestDTO;
import br.com.ldr.devshe.dto.EmpresaResponseDTO;
import br.com.ldr.devshe.exceptions.NotFoundError;
import br.com.ldr.devshe.services.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Secured({"ROLE_ADMIN", "ROLE_USER"})
@RestController
@RequestMapping("/api/v1/empresa")
@Validated
public class EmpresaController extends BaseController {

    private final EmpresaService empresaService;

    @Autowired
    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> criar(
            @RequestBody @Valid EmpresaRequestDTO dto) throws NotFoundError {
        EmpresaResponseDTO response = empresaService.criarEmpresa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<EmpresaResponseDTO> getEmpresaByUuid(
            @PathVariable String uuid) throws NotFoundError {
        Empresa empresa = empresaService.getEmpresaByUuid(uuid);
        EmpresaResponseDTO empresaResponseDTO = new EmpresaResponseDTO(empresa);
        return ResponseEntity.ok(empresaResponseDTO);
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<EmpresaResponseDTO>> pesquisarEmpresas(
            @RequestParam String nome) throws NotFoundError {
        List<Empresa> empresas = empresaService.pesquisarEmpresas(nome);
        List<EmpresaResponseDTO> empresasDTO = new ArrayList<>();
        empresas.forEach((empresa) -> {
            EmpresaResponseDTO empresaResponseDTO = new EmpresaResponseDTO(empresa);
            empresasDTO.add(empresaResponseDTO);
        });
        return ResponseEntity.ok(empresasDTO);
    }
}
