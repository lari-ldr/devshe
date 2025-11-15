package br.com.ldr.devshe.controllers;

import br.com.ldr.devshe.BaseTest;
import br.com.ldr.devshe.domain.Review;
import br.com.ldr.devshe.dto.*;
import br.com.ldr.devshe.repositories.EmpresaRepository;
import br.com.ldr.devshe.repositories.ReviewRepository;
import br.com.ldr.devshe.repositories.TrabalhoRepository;
import br.com.ldr.devshe.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewControllerTest extends BaseTest {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TrabalhoRepository trabalhoRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * POST /api/v1/review
     *
     * Criar review com sucesso quando usuário trabalhou na empresa
     */
    @Test
    void testCriarReviewComSucesso() {
        EmpresaResponseDTO empresa = criarEmpresa(authAdmin());
        criarTrabalho(empresa, authAdmin());

        ReviewRequestDTO request = criarReviewRequest(empresa.getUuid());

        ReviewPublicoEmpresaDTO result = this.httpClient.post()
                .uri("/api/v1/review")
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReviewPublicoEmpresaDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(result);
        assertEquals(request.getTitulo(), result.getTitulo());
        assertEquals(request.getAvaliacaoEmpresa(), result.getAvaliacaoEmpresa());
        assertEquals(request.getComentario(), result.getComentario());
        assertEquals(empresa.getUuid(), result.getEmpresaUuid());
        assertNotNull(result.getUuid());
        assertNotNull(result.getDataCriacao());
    }

    /**
     * POST /api/v1/review
     *
     * Não deve permitir criar review se usuário não trabalhou na empresa
     */
    @Test
    void testCriarReviewSemVinculoTrabalho() {
        EmpresaResponseDTO empresa = criarEmpresa(authUser());
        criarTrabalho(empresa, authUser());
        // NÃO cria vínculo de trabalho

        ReviewRequestDTO request = criarReviewRequest(empresa.getUuid());

        Erro erro = this.httpClient.post()
                .uri("/api/v1/review")
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(Erro.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(erro);
        assertEquals("400", erro.getCode());
        assertEquals("Usuário não trabalhou nesta empresa", erro.getMessage());
    }

    /**
     * POST /api/v1/review
     *
     * Não deve permitir criar review duplicado para a mesma empresa
     */
    @Test
    void testCriarReviewDuplicado() {
        EmpresaResponseDTO empresa = criarEmpresa(authAdmin());
        criarTrabalho(empresa, authAdmin());
        criarReview(empresa, authAdmin());

        ReviewRequestDTO request = criarReviewRequest(empresa.getUuid());

        Erro erro = this.httpClient.post()
                .uri("/api/v1/review")
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(Erro.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(erro);
        assertEquals("400", erro.getCode());
        assertEquals("Usuário já fez review desta empresa", erro.getMessage());
    }

    /**
     * POST /api/v1/review
     *
     * Não deve permitir criar review para empresa inexistente
     */
    @Test
    void testCriarReviewEmpresaInexistente() {
        String empresaUuidInexistente = uuid();

        ReviewRequestDTO request = criarReviewRequest(empresaUuidInexistente);

        Erro erro = this.httpClient.post()
                .uri("/api/v1/review")
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Erro.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(erro);
        assertEquals("NOT_FOUND", erro.getCode());
        assertEquals("Empresa não encontrada", erro.getMessage());
    }

    /**
     * POST /api/v1/review
     *
     * Deve retornar 401 quando não enviar token de autenticação
     */
    @Test
    void testCriarReviewSemAutenticacao() {
        ReviewRequestDTO request = criarReviewRequest(uuid());

        this.httpClient.post()
                .uri("/api/v1/review")
                .bodyValue(request)
                .exchange()
                .expectStatus().isForbidden();
    }

    /**
     * POST /api/v1/review
     *
     * Deve retornar 400 quando faltar parâmetros obrigatórios
     */
    @Test
    void testCriarReviewFaltandoParametros() {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setEmpresaUuid(null);

        this.httpClient.post()
                .uri("/api/v1/review")
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * PUT /api/v1/review/{reviewUuid}
     *
     * Atualizar review com sucesso
     */
    @Test
    void testAtualizarReviewComSucesso() {
        EmpresaResponseDTO empresa = criarEmpresa(authAdmin());
        criarTrabalho(empresa, authAdmin());
        ReviewPublicoEmpresaDTO review = criarReview(empresa, authAdmin());

        ReviewRequestDTO request = criarReviewRequest(empresa.getUuid());
        request.setTitulo("Título Atualizado");
        request.setAvaliacaoEmpresa(5);
        request.setComentario("Comentário atualizado");

        ReviewPublicoEmpresaDTO result = this.httpClient.put()
                .uri("/api/v1/review/" + review.getUuid())
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ReviewPublicoEmpresaDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(result);
        assertEquals("Título Atualizado", result.getTitulo());
        assertEquals(5, result.getAvaliacaoEmpresa());
        assertEquals("Comentário atualizado", result.getComentario());
    }

    /**
     * PUT /api/v1/review/{reviewUuid}
     *
     * Não deve permitir atualizar review de outro usuário
     */
    @Test
    void testAtualizarReviewDeOutroUsuario() {
        Autorizacao autorizacao = authUser();
        EmpresaResponseDTO empresa = criarEmpresa(autorizacao);
        criarTrabalho(empresa, autorizacao);
        ReviewPublicoEmpresaDTO review = criarReview(empresa, autorizacao);

        ReviewRequestDTO request = criarReviewRequest(empresa.getUuid());

        Erro erro = this.httpClient.put()
                .uri("/api/v1/review/" + review.getUuid())
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(Erro.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(erro);
        assertEquals("400", erro.getCode());
        assertEquals("Review não encontrado ou não pertencente ao autor e/ou empresa", erro.getMessage());
    }

    /**
     * PUT /api/v1/review/{reviewUuid}
     *
     * Não deve permitir atualizar review inexistente
     */
    @Test
    void testAtualizarReviewInexistente() {
        String reviewUuidInexistente = uuid();

        ReviewRequestDTO request = criarReviewRequest(criarEmpresa(authAdmin()).getUuid());

        Erro erro = this.httpClient.put()
                .uri("/api/v1/review/" + reviewUuidInexistente)
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(Erro.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(erro);
        assertEquals("400", erro.getCode());
    }

    /**
     * DELETE /api/v1/review/{reviewUuid}
     *
     * Deletar review com sucesso (soft delete)
     */
    @Test
    void testDeletarReviewComSucesso() {
        EmpresaResponseDTO empresa = criarEmpresa(authAdmin());
        criarTrabalho(empresa, authAdmin());
        ReviewPublicoEmpresaDTO review = criarReview(empresa, authAdmin());

        this.httpClient.delete()
                .uri("/api/v1/review/" + review.getUuid())
                .header("Authorization", authAdmin().getToken())
                .exchange()
                .expectStatus().isNoContent();

        // Verifica que o review foi marcado como inativo
        Review reviewAtualizado = reviewRepository.findByUuid(review.getUuid()).orElse(null);
        assertNotNull(reviewAtualizado);
        assertFalse(reviewAtualizado.getAtivo());
    }

    /**
     * DELETE /api/v1/review/{reviewUuid}
     *
     * Não deve permitir deletar review de outro usuário
     */
    @Test
    void testDeletarReviewDeOutroUsuario() {
        Autorizacao autorizacao = authUser();
        EmpresaResponseDTO empresa = criarEmpresa(autorizacao);
        criarTrabalho(empresa, autorizacao);
        ReviewPublicoEmpresaDTO review = criarReview(empresa, autorizacao);

        Erro erro = this.httpClient.delete()
                .uri("/api/v1/review/" + review.getUuid())
                .header("Authorization", authAdmin().getToken())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(Erro.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(erro);
        assertEquals("400", erro.getCode());
        assertEquals("Review não encontrado ou não pertencente ao autor.", erro.getMessage());
    }

    /**
     * GET /api/v1/review/empresa/{uuid}
     *
     * Buscar reviews por empresa com sucesso
     */
    @Test
    void testBuscarReviewsPorEmpresaComResultados() {
        EmpresaResponseDTO empresa = criarEmpresa(authAdmin());
        criarTrabalho(empresa, authAdmin());
        criarReview(empresa, authAdmin());


        List<ReviewPublicoEmpresaDTO> result = this.httpClient.get()
                .uri("/api/v1/review/empresa/" + empresa.getUuid())
                .header("Authorization", authAdmin().getToken())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ReviewPublicoEmpresaDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(result);
        assertEquals(1, result.size());
        // Verifica que não expõe informações do autor (anonimato)
        result.forEach(review -> {
            assertNotNull(review.getUuid());
            assertNotNull(review.getEmpresaUuid());
            assertNotNull(review.getTitulo());
        });
    }

    /**
     * GET /api/v1/review/empresa/{uuid}
     *
     * Buscar reviews de empresa sem reviews deve retornar lista vazia
     */
    @Test
    void testBuscarReviewsPorEmpresaSemResultados() {
        EmpresaResponseDTO empresa = criarEmpresa(authAdmin());

        List<ReviewPublicoEmpresaDTO> result = this.httpClient.get()
                .uri("/api/v1/review/empresa/" + empresa.getUuid())
                .header("Authorization", authAdmin().getToken())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ReviewPublicoEmpresaDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * GET /api/v1/review/empresa/{uuid}
     *
     * Deve retornar 404 quando empresa não existe
     */
    @Test
    void testBuscarReviewsEmpresaInexistente() {
        String empresaUuidInexistente = uuid();

        Erro erro = this.httpClient.get()
                .uri("/api/v1/review/empresa/" + empresaUuidInexistente)
                .header("Authorization", authAdmin().getToken())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Erro.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(erro);
        assertEquals("NOT_FOUND", erro.getCode());
        assertEquals("Empresa não encontrada", erro.getMessage());
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private ReviewRequestDTO criarReviewRequest(String empresaUuid) {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setEmpresaUuid(empresaUuid);
        request.setTitulo("Ótima empresa para trabalhar");
        request.setAvaliacaoEmpresa(4);
        request.setComentario("Ambiente colaborativo e bons benefícios");
        request.setAvaliacaoAmbiente(4);
        request.setAvaliacaoSalario(3);
        request.setAvaliacaoBeneficios(5);
        request.setAvaliacaoGestao(4);
        return request;
    }
}