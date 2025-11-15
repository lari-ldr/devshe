package br.com.ldr.devshe.controllers;

import br.com.ldr.devshe.dto.ReviewPublicoEmpresaDTO;
import br.com.ldr.devshe.dto.ReviewRequestDTO;
import br.com.ldr.devshe.exceptions.NotFoundError;
import br.com.ldr.devshe.services.ReviewService;
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
@RequestMapping("/api/v1/review")
@Validated
public class ReviewController extends BaseController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewPublicoEmpresaDTO> criar(
            @RequestBody @Valid ReviewRequestDTO dto) throws NotFoundError {
        ReviewPublicoEmpresaDTO review = reviewService.criarReview(getUsuario(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PutMapping("/{reviewUuid}")
    public ResponseEntity<ReviewPublicoEmpresaDTO> atualizar(
            @PathVariable String reviewUuid,
            @RequestBody ReviewRequestDTO dto) throws NotFoundError {
        ReviewPublicoEmpresaDTO review = reviewService.atualizarReview(getUsuario(), reviewUuid, dto);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletar(@PathVariable String uuid) {
        reviewService.deletarReview(getUsuario(), uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/empresa/{uuid}")
    public ResponseEntity<List<ReviewPublicoEmpresaDTO>> buscarReviewPorEmpresa(
            @PathVariable String uuid) throws NotFoundError {
        return ResponseEntity.ok(reviewService.buscarReviewPorEmpresa(uuid));
    }
}
