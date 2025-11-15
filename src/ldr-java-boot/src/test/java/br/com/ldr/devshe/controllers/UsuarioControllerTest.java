package br.com.ldr.devshe.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.ldr.devshe.BaseTest;
import br.com.ldr.devshe.dto.Autorizacao;
import br.com.ldr.devshe.dto.Erro;
import br.com.ldr.devshe.dto.UsuarioDTO;
import br.com.ldr.devshe.dto.UsuarioRequest;
import br.com.ldr.devshe.repositories.UsuarioRepository;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsuarioControllerTest extends BaseTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * PUT /api/v1/user/{uuid}
     *
     * Se o usuário não tiver permissão de acesso ao endpoint então o código 403
     * deverá ser retornado
     */
    @Test
    void testUpdateUsuarioAcessoNegadoRetornar403() {
        UsuarioDTO original = this.criarUsuario(getPerfilAdmin(), uuid() + "@teste.com", uuid());

        UsuarioRequest dto = createUsuarioRequest();

        // token invalido
        this.httpClient.put().uri("/api/v1/user/" + original.getUuid())
                .header("Authorization", uuid())
                .bodyValue(dto)
                .exchange()
                .expectStatus().isForbidden();

        // sem token
        this.httpClient.put().uri("/api/v1/user/" + original.getUuid())
                .bodyValue(dto)
                .exchange()
                .expectStatus().isForbidden();
    }

    /**
     * PUT /api/v1/user/{uuid}
     *
     * Se o identificador do usuário for inválido (inexistente), o código 404 deve
     * ser retornado
     */
    @Test
    void testUpdateUsuarioInexistente() {
        UsuarioRequest dto = createUsuarioRequest();

        Erro erro = this.httpClient.put().uri("/api/v1/user/" + uuid())
                .header("Authorization", authAdmin().getToken())
                .bodyValue(dto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Erro.class).returnResult().getResponseBody();
        assertNotNull(erro);
        assertEquals("NOT_FOUND", erro.getCode());
        assertEquals("Usuário não encontrado.", erro.getMessage());
    }

    /**
     * PUT /api/v1/user/{uuid}
     *
     * Editando um usuário com todas as condições satisfeitas
     */
    @Test
    void testUpdateUsuarioOk() {
        UsuarioDTO original = this.criarUsuario(getPerfilAdmin(), uuid() + "@teste.com", uuid());

        UsuarioRequest dto = createUsuarioRequest();

        UsuarioDTO result = this.httpClient.put().uri("/api/v1/user/" + original.getUuid())
                .header("Authorization", this.authAdmin().getToken())
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioDTO.class).returnResult().getResponseBody();

        validarCriacaoEdicaoBemSucessidida(dto, result);
    }

    private void validarCriacaoEdicaoBemSucessidida(UsuarioRequest request, UsuarioDTO result) {
        assertNotNull(result);
        assertEquals(request.getUsername(), result.getUsername());
        assertEquals(request.getNome(), result.getNome());
        assertEquals(request.getProfile(), result.getProfile());
        assertNotNull(result.getUuid());
        assertTrue(result.isAtivo());

        Autorizacao authCriado = this.autenticar(request.getUsername(), request.getPassword());
        assertNotNull(authCriado);
    }

    /**
     * PUT /api/v1/user/{uuid}
     *
     * Se na hora da edição for informado um email ja em uso pertencente
     * a outro usuario retornar erro
     */
    @Test
    void testUpdateUsuarioAlterandoParaEmailUsadoPorOutroUsuarioRetornarErro400() {
        String email = uuid() + "@teste.com";
        this.criarUsuario(getPerfilAdmin(), email, uuid());
        UsuarioDTO usuarioASerEditado = this.criarUsuario(getPerfilAdmin(), uuid(), uuid());

        UsuarioRequest dto = createUsuarioRequest();
        dto.setUsername(email);
        Erro erro = this.httpClient.put().uri("/api/v1/user/" + usuarioASerEditado.getUuid())
                .header("Authorization", authAdmin().getToken())
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(Erro.class).returnResult().getResponseBody();
        assertNotNull(erro);
        assertEquals("400", erro.getCode());
        assertEquals("Já existe outro usuário cadastrado com este e-mail", erro.getMessage());

    }

    /**
     * PUT /api/v1/user/{uuid}
     *
     * Caso nao seja enviado todos os parametros para a criação do usuario
     * deve retornar erro 400
     */
    @Test
    void testEdicaoUsuarioFaltandoParametroObrigatorioRetornarErro() {
        UsuarioRequest request = createUsuarioRequest();

        // Sem usernama
        request.setUsername(null);
        badRequestEdited(request);
        request.setUsername("");
        badRequestEdited(request);

        // Sem nome
        request = createUsuarioRequest();
        request.setNome(null);
        badRequestEdited(request);
        request.setNome("");
        badRequestEdited(request);

        // Sem profile
        request = createUsuarioRequest();
        request.setProfile(null);
        badRequestEdited(request);
        request.setProfile("");
        badRequestEdited(request);
    }

    private void badRequestEdited(UsuarioRequest request) {
        UsuarioDTO original = this.criarUsuario(getPerfilAdmin(), uuid() + "@teste.com", uuid());

        this.httpClient.put().uri("/api/v1/user/" + original.getUuid())
                .header("Authorization", this.authAdmin().getToken())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /**
     * GET /api/v1/user
     *
     * Se o usuário não tiver permissão de acesso ao endpoint então o código 403
     * deverá ser retornado
     */
    @Test
    void testGETUsuarioAcessoNegadoRetornar403() {
        // token invalido
        this.httpClient.get().uri("/api/v1/user")
                .header("Authorization", uuid())
                .exchange()
                .expectStatus().isForbidden();

        // sem token
        this.httpClient.get().uri("/api/v1/user")
                .exchange()
                .expectStatus().isForbidden();
    }

}