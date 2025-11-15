package br.com.ldr.devshe.controllers;


import br.com.ldr.devshe.BaseTest;
import br.com.ldr.devshe.dto.Autorizacao;
import br.com.ldr.devshe.dto.Erro;
import br.com.ldr.devshe.dto.UsuarioDTO;
import br.com.ldr.devshe.dto.UsuarioRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterControllerTest extends BaseTest {

    /**
     * POST /api/v1/register
     *
     * Registro do usuário com todas as condições satisfeitas
     */
    @Test
    void testCriarUsuario() {
        UsuarioRequest request = createUsuarioRequest();

        Autorizacao autorizacao = this.httpClient.post().uri("/api/v1/register")
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(Autorizacao.class).returnResult().getResponseBody();

    }

    /**
     * POST /api/v1/register
     *
     * Não podem existir dois usuários com mesmo login cadastrado, o email deve ser unico.
     * Deve ser retornado código 400 informando a causa do problema
     */
    @Test
    void testCreateUsuarioEmailDuplicado() {
        UsuarioRequest request = createUsuarioRequest();


        this.httpClient.post().uri("/api/v1/register")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();

        // agora vamos tentar novamente criar outro usuario com o mesmo request
        Erro erro = this.httpClient.post().uri("/api/v1/register")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(Erro.class).returnResult().getResponseBody();
        assertNotNull(erro);
        assertEquals("400", erro.getCode());
        assertEquals("Já existe um usuário cadastrado com este e-mail", erro.getMessage());

    }

    /**
     * POST /api/v1/register
     *
     * Caso seja enviado perfil invalido deve retornar erro para o usuario
     */
    @Test
    void testCreateUsuarioPerfilInvalido() {
        UsuarioRequest request = createUsuarioRequest();
        request.setProfile(uuid());

        Erro erro = this.httpClient.post().uri("/api/v1/register")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(Erro.class).returnResult().getResponseBody();
        assertNotNull(erro);
        assertEquals("400", erro.getCode());
        assertEquals("Perfil desconhecido", erro.getMessage());
    }

    /**
     * POST /api/v1/register
     *
     * Caso nao seja enviado todos os parametros para a criação do usuario
     * deve retornar erro 400
     */
    @Test
    void testCreateUsuarioFaltandoParametroObrigatorioRetornarErro() {
        UsuarioRequest request = createUsuarioRequest();

        // Sem usernama
        request.setUsername(null);
        badRequestCreated(request);
        request.setUsername("");
        badRequestCreated(request);

        // sem senha
        request = createUsuarioRequest();
        request.setPassword(null);
        badRequestCreated(request);
        request.setPassword("");
        badRequestCreated(request);

        // Sem nome
        request = createUsuarioRequest();
        request.setNome(null);
        badRequestCreated(request);
        request.setNome("");
        badRequestCreated(request);

        // Sem profile
        request = createUsuarioRequest();
        request.setProfile(null);
        badRequestCreated(request);
        request.setProfile("");
        badRequestCreated(request);
    }

    private void badRequestCreated(UsuarioRequest request) {
        this.httpClient.post().uri("/api/v1/register")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(Erro.class).returnResult().getResponseBody();
    }


}
