package br.com.ldr.devshe.controllers;


import br.com.ldr.devshe.BaseTest;
import br.com.ldr.devshe.dto.EmpresaResponseDTO;
import br.com.ldr.devshe.dto.Erro;
import br.com.ldr.devshe.repositories.EmpresaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmpresaControllerTest extends BaseTest {

    @Autowired
    private EmpresaRepository empresaRepository;

    /**
     * GET /api/v1/empresa/{uuid}
     *
     * Buscar empresa por UUID com sucesso
     */
    @Test
    void testBuscarEmpresaPorUuidComSucesso() {
        EmpresaResponseDTO empresa = criarEmpresa(authAdmin());

        EmpresaResponseDTO result = this.httpClient.get()
                .uri("/api/v1/empresa/" + empresa.getUuid())
                .header("Authorization", authAdmin().getToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmpresaResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(result);
        assertEquals(empresa.getUuid(), result.getUuid());
        assertEquals(empresa.getNome(), result.getNome());
        assertEquals(empresa.getCnpj(), result.getCnpj());
    }

    /**
     * GET /api/v1/empresa/{uuid}
     *
     * Deve retornar 404 quando empresa não for encontrada
     */
    @Test
    void testBuscarEmpresaPorUuidNaoEncontrada() {
        String uuidInexistente = uuid();

        Erro erro = this.httpClient.get()
                .uri("/api/v1/empresa/" + uuidInexistente)
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

    /**
     * GET /api/v1/empresa/{uuid}
     *
     * Deve retornar 401 quando não enviar token de autenticação
     */
    @Test
    void testBuscarEmpresaSemAutenticacao() {
        EmpresaResponseDTO empresa = criarEmpresa(authAdmin());

        this.httpClient.get()
                .uri("/api/v1/empresa/" + empresa.getUuid())
                .exchange()
                .expectStatus().isForbidden();
    }

    /**
     * GET /api/v1/empresa/pesquisar?nome=
     *
     * Pesquisar empresas sem resultados deve retornar lista vazia
     */
    @Test
    void testPesquisarEmpresasSemResultados() {
        criarEmpresa(authAdmin());

        List<EmpresaResponseDTO> result = this.httpClient.get()
                .uri("/api/v1/empresa/pesquisar?nome=EmpresaInexistente")
                .header("Authorization", authAdmin().getToken())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmpresaResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * GET /api/v1/empresa/pesquisar?nome=
     *
     * Deve retornar 401 quando não enviar token de autenticação
     */
    @Test
    void testPesquisarEmpresasSemAutenticacao() {
        this.httpClient.get()
                .uri("/api/v1/empresa/pesquisar?nome=Tech")
                .exchange()
                .expectStatus().isForbidden();
    }

}