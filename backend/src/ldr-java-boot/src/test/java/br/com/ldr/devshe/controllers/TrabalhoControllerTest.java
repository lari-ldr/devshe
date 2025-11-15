package br.com.ldr.devshe.controllers;

import br.com.ldr.devshe.BaseTest;
import br.com.ldr.devshe.UtilData;
import br.com.ldr.devshe.dto.EmpresaResponseDTO;
import br.com.ldr.devshe.dto.Erro;
import br.com.ldr.devshe.dto.TrabalhoRequestDTO;
import br.com.ldr.devshe.dto.TrabalhoResponseDTO;
import br.com.ldr.devshe.repositories.EmpresaRepository;
import br.com.ldr.devshe.repositories.TrabalhoRepository;
import br.com.ldr.devshe.repositories.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TrabalhoControllerTest extends BaseTest {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TrabalhoRepository trabalhoRepository;

    @AfterEach
    void limparDados() {
        trabalhoRepository.deleteAll();
        empresaRepository.deleteAll();
    }

    /**
     * POST /api/v1/trabalho
     *
     * Criar trabalho com sucesso
     */
    @Test
    void testCriarTrabalhoComSucesso() {
        EmpresaResponseDTO empresa = criarEmpresa(authAdmin());

        TrabalhoRequestDTO request = criarTrabalhoRequest(empresa.getUuid());

        TrabalhoResponseDTO result = this.httpClient.post()
                .uri("/api/v1/trabalho")
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(TrabalhoResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(result);
        assertEquals(request.getCargo(), result.getCargo());
        assertEquals(request.getDataInicio(), result.getDataInicio());
        assertEquals(request.getDataFim(), result.getDataFim());
        assertEquals(request.getEstaTrabalhando(), result.getEstaTrabalhando());
        assertEquals(empresa.getUuid(), result.getEmpresaUuid());
        assertEquals(empresa.getNome(), result.getEmpresaNome());
        assertNotNull(result.getUuid());
    }

    /**
     * POST /api/v1/trabalho
     *
     * Não deve permitir criar trabalho duplicado para a mesma empresa
     */
    @Test
    void testCriarTrabalhoDuplicado() {
        EmpresaResponseDTO empresa = criarEmpresa(authAdmin());
        criarTrabalho(empresa, authAdmin());

        TrabalhoRequestDTO request = criarTrabalhoRequest(empresa.getUuid());

        Erro erro = this.httpClient.post()
                .uri("/api/v1/trabalho")
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(Erro.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(erro);
        assertEquals("400", erro.getCode());
        assertEquals("Usuário já possui registro de trabalho nessa empresa", erro.getMessage());
    }

    /**
     * POST /api/v1/trabalho
     *
     * Não deve permitir criar trabalho para empresa inexistente
     */
    @Test
    void testCriarTrabalhoEmpresaInexistente() {
        String empresaUuidInexistente = UUID.randomUUID().toString();

        TrabalhoRequestDTO request = criarTrabalhoRequest(empresaUuidInexistente);

        Erro erro = this.httpClient.post()
                .uri("/api/v1/trabalho")
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
     * POST /api/v1/trabalho
     *
     * Deve criar trabalho com estaTrabalhando = true
     */
    @Test
    void testCriarTrabalhoAtivo() {
        EmpresaResponseDTO empresa = criarEmpresa(authAdmin());

        TrabalhoRequestDTO request = criarTrabalhoRequest(empresa.getUuid());
        request.setEstaTrabalhando(true);
        request.setDataFim(null);

        TrabalhoResponseDTO result = this.httpClient.post()
                .uri("/api/v1/trabalho")
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(TrabalhoResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(result);
        assertTrue(result.getEstaTrabalhando());
        assertNull(result.getDataFim());
    }

    /**
     * POST /api/v1/trabalho
     *
     * Deve criar trabalho com estaTrabalhando = false
     */
    @Test
    void testCriarTrabalhoInativo() {
        EmpresaResponseDTO empresa = criarEmpresa(authAdmin());

        TrabalhoRequestDTO request = criarTrabalhoRequest(empresa.getUuid());
        request.setEstaTrabalhando(false);
        request.setDataFim(UtilData.localDateToString(LocalDate.now().minusMonths(1)));

        TrabalhoResponseDTO result = this.httpClient.post()
                .uri("/api/v1/trabalho")
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(TrabalhoResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(result);
        assertFalse(result.getEstaTrabalhando());
        assertNotNull(result.getDataFim());
    }

    /**
     * POST /api/v1/trabalho
     *
     * Deve retornar 401 quando não enviar token de autenticação
     */
    @Test
    void testCriarTrabalhoSemAutenticacao() {
        EmpresaResponseDTO empresa = criarEmpresa(authAdmin());

        TrabalhoRequestDTO request = criarTrabalhoRequest(empresa.getUuid());

        this.httpClient.post()
                .uri("/api/v1/trabalho")
                .bodyValue(request)
                .exchange()
                .expectStatus().isForbidden();
    }

    /**
     * POST /api/v1/trabalho
     *
     * Deve retornar 400 quando faltar parâmetros obrigatórios
     */
    @Test
    void testCriarTrabalhoFaltandoParametros() {

        // Sem empresaUuid
        TrabalhoRequestDTO request = new TrabalhoRequestDTO();
        request.setEmpresaUuid(null);
        request.setCargo("Desenvolvedor");

        this.httpClient.post()
                .uri("/api/v1/trabalho")
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();

        // Sem cargo
        request = criarTrabalhoRequest(criarEmpresa(authAdmin()).getUuid());
        request.setCargo(null);

        this.httpClient.post()
                .uri("/api/v1/trabalho")
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();

        // Sem dataInicio
        request = criarTrabalhoRequest(criarEmpresa(authAdmin()).getUuid());
        request.setDataInicio(null);

        this.httpClient.post()
                .uri("/api/v1/trabalho")
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * GET /api/v1/trabalho/meus
     *
     * Listar trabalhos do usuário com resultados
     */
    @Test
    void testListarMeusTrabalhosComResultados() {

        EmpresaResponseDTO empresa1 = criarEmpresa(authAdmin());
        EmpresaResponseDTO empresa2 = criarEmpresa(authAdmin());
        EmpresaResponseDTO empresa3 = criarEmpresa(authAdmin());

        criarTrabalho(empresa1, authAdmin());
        criarTrabalho(empresa2, authAdmin());
        criarTrabalho(empresa3, authAdmin());

        List<TrabalhoResponseDTO> result = this.httpClient.get()
                .uri("/api/v1/trabalho/meus")
                .header("Authorization", authAdmin().getToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TrabalhoResponseDTO>>() {})
                .returnResult()
                .getResponseBody();

        assertNotNull(result);
        assertEquals(3, result.size());

        // Verifica que todos os trabalhos pertencem ao usuário
        result.forEach(trabalho -> {
            assertNotNull(trabalho.getUuid());
            assertNotNull(trabalho.getEmpresaUuid());
            assertNotNull(trabalho.getEmpresaNome());
            assertNotNull(trabalho.getCargo());
        });
    }

    /**
     * GET /api/v1/trabalho/meus
     *
     * Listar trabalhos do usuário sem resultados deve retornar lista vazia
     */
    @Test
    void testListarMeusTrabalhosSemResultados() {

        List<TrabalhoResponseDTO> result = this.httpClient.get()
                .uri("/api/v1/trabalho/meus")
                .header("Authorization", authAdmin().getToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TrabalhoResponseDTO>>() {})
                .returnResult()
                .getResponseBody();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * GET /api/v1/trabalho/meus
     *
     * Deve listar apenas trabalhos do usuário autenticado
     */
    @Test
    void testListarMeusTrabalhosApenasDoUsuarioAutenticado() {
        EmpresaResponseDTO empresa1 = criarEmpresa(authAdmin());
        EmpresaResponseDTO empresa2 = criarEmpresa(authAdmin());

        // Cria trabalhos para ambos usuários
        criarTrabalho(empresa1, authAdmin());
        criarTrabalho(empresa2, authUser());

        List<TrabalhoResponseDTO> result = this.httpClient.get()
                .uri("/api/v1/trabalho/meus")
                .header("Authorization", authAdmin().getToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TrabalhoResponseDTO>>() {})
                .returnResult()
                .getResponseBody();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * GET /api/v1/trabalho/meus
     *
     * Deve retornar 401 quando não enviar token de autenticação
     */
    @Test
    void testListarMeusTrabalhosSemAutenticacao() {
        this.httpClient.get()
                .uri("/api/v1/trabalho/meus")
                .exchange()
                .expectStatus().isForbidden();
    }

    /**
     * Teste integrado: Criar trabalho e depois listá-lo
     */
    @Test
    void testFluxoCompletoCriarEListar() {
        EmpresaResponseDTO empresa = criarEmpresa(authAdmin());

        TrabalhoRequestDTO request = criarTrabalhoRequest(empresa.getUuid());

        // Criar trabalho
        TrabalhoResponseDTO trabalhoCriado = this.httpClient.post()
                .uri("/api/v1/trabalho")
                .header("Authorization", authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(TrabalhoResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(trabalhoCriado);

        // Listar trabalhos
        List<TrabalhoResponseDTO> trabalhos = this.httpClient.get()
                .uri("/api/v1/trabalho/meus")
                .header("Authorization", authAdmin().getToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TrabalhoResponseDTO>>() {})
                .returnResult()
                .getResponseBody();

        assertNotNull(trabalhos);
        assertEquals(1, trabalhos.size());
        assertEquals(trabalhoCriado.getUuid(), trabalhos.get(0).getUuid());
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private TrabalhoRequestDTO criarTrabalhoRequest(String empresaUuid) {
        TrabalhoRequestDTO request = new TrabalhoRequestDTO();
        request.setEmpresaUuid(empresaUuid);
        request.setCargo("Desenvolvedor Full Stack");
        request.setDataInicio(UtilData.localDateToString(LocalDate.now().minusYears(2)));
        request.setDataFim(UtilData.localDateToString(LocalDate.now().minusMonths(6)));
        request.setEstaTrabalhando(false);
        return request;
    }

}