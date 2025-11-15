package br.com.ldr.devshe.services;

import br.com.ldr.devshe.domain.Empresa;
import br.com.ldr.devshe.domain.Review;
import br.com.ldr.devshe.domain.Usuario;
import br.com.ldr.devshe.dto.ReviewPublicoEmpresaDTO;
import br.com.ldr.devshe.dto.ReviewRequestDTO;
import br.com.ldr.devshe.exceptions.NotFoundError;
import br.com.ldr.devshe.exceptions.ServiceError;
import br.com.ldr.devshe.repositories.ReviewRepository;
import br.com.ldr.devshe.repositories.TrabalhoRepository;
import br.com.ldr.devshe.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final EmpresaService empresaService;
    private final ReviewRepository reviewRepository;
    private final UsuarioRepository usuarioRepository;
    private final TrabalhoRepository trabalhoRepository;

    @Autowired
    public ReviewService(EmpresaService empresaService, ReviewRepository reviewRepository, UsuarioRepository usuarioRepository, TrabalhoRepository trabalhoRepository) {
        this.empresaService = empresaService;
        this.reviewRepository = reviewRepository;
        this.usuarioRepository = usuarioRepository;
        this.trabalhoRepository = trabalhoRepository;
    }

    @Transactional
    public ReviewPublicoEmpresaDTO criarReview(Usuario usuario, ReviewRequestDTO request) throws NotFoundError {

        Empresa empresa = empresaService.getEmpresaByUuid(request.getEmpresaUuid());
        if (!trabalhoRepository.existsByUsuarioAndEmpresa(usuario, empresa)) {
            throw new ServiceError("Usuário não trabalhou nesta empresa");
        }

        if (reviewRepository.existsByAutorAndEmpresa(usuario, empresa)) {
            throw new ServiceError("Usuário já fez review desta empresa");
        }

        Review review = new Review(request, usuario, empresa);

        review = reviewRepository.save(review);
        return new ReviewPublicoEmpresaDTO(review);
    }

    @Transactional
    public ReviewPublicoEmpresaDTO atualizarReview(Usuario usuario, String reviewUuid, ReviewRequestDTO request) throws NotFoundError {
        Empresa empresa = empresaService.getEmpresaByUuid(request.getEmpresaUuid());
        Optional<Review> reviewOpt = reviewRepository.findByUuidAndAutorAndEmpresaAndAtivoTrue(reviewUuid, usuario, empresa);
        if (reviewOpt.isEmpty()) {
            throw new ServiceError("Review não encontrado ou não pertencente ao autor e/ou empresa");
        }

        Review review = reviewOpt.get();
        review.setTitulo(request.getTitulo());
        review.setAvaliacaoEmpresa(request.getAvaliacaoEmpresa());
        review.setComentario(request.getComentario());
        review.setAvaliacaoAmbiente(request.getAvaliacaoAmbiente());
        review.setAvaliacaoSalario(request.getAvaliacaoSalario());
        review.setAvaliacaoBeneficios(request.getAvaliacaoBeneficios());
        review.setAvaliacaoGestao(request.getAvaliacaoGestao());

        review = reviewRepository.save(review);
        return new ReviewPublicoEmpresaDTO(review);
    }

    @Transactional
    public void deletarReview(Usuario usuario, String reviewUuid) {
        Optional<Review> reviewOptional = reviewRepository.findByUuidAndAutorAndAtivoTrue(reviewUuid, usuario);
        if (reviewOptional.isEmpty()) {
            throw new ServiceError("Review não encontrado ou não pertencente ao autor.");
        }
        Review review = reviewOptional.get();
        review.setAtivo(false);
        reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewPublicoEmpresaDTO> buscarReviewPorEmpresa(String empresaUuid) throws NotFoundError {
        Empresa empresa = empresaService.getEmpresaByUuid(empresaUuid);
        List<Review> reviews = reviewRepository.findAllByEmpresaAndAtivoTrue(empresa);
        if (reviews.isEmpty()) return new ArrayList<>();

        List<ReviewPublicoEmpresaDTO> reviewPublicoEmpresaDTOList = new ArrayList<>();
        reviews.forEach(review -> {
            reviewPublicoEmpresaDTOList.add(new ReviewPublicoEmpresaDTO(review));
        });
        return reviewPublicoEmpresaDTOList;
    }
}
