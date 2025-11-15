package br.com.ldr.devshe;

import br.com.ldr.devshe.domain.Empresa;
import br.com.ldr.devshe.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.ldr.devshe.domain.Perfil;
import br.com.ldr.devshe.domain.Permissao;
import br.com.ldr.devshe.repositories.PerfilRepository;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class BaseTest {

	@Autowired
	protected WebTestClient httpClient;
	
    @Autowired
    protected PerfilRepository perfilRepository;

	private Random rand;

	@BeforeEach
	public void beforeEach() {
		this.httpClient = this.httpClient.mutate().responseTimeout(Duration.ofHours(4)).build();
	}
	
	protected String uuid() {
		return UUID.randomUUID().toString();
	}
	
    protected Perfil getPerfilAdmin() {
        return perfilRepository.findByNome("Administrador");
    }
	
    protected UsuarioRequest createUsuarioRequest(){
        UsuarioRequest request = new UsuarioRequest();
        request.setUsername(uuid());
        request.setPassword(uuid());
        request.setNome(uuid());
        request.setProfile(getPerfilAdmin().getUuid());
        request.setAtivo(true);
        return request;
    }
    
    protected UsuarioDTO criarUsuario(Perfil perfil, String email, String senha, boolean ativo){
        UsuarioRequest request = new UsuarioRequest();

        request.setUsername(email);
        request.setNome("Nome " + uuid());
        request.setPassword(senha);
        request.setProfile(perfil.getUuid());
        request.setAtivo(ativo);

        return this.httpClient.post().uri("/api/v1/user/create")
                .bodyValue(request)
				.header("Authorization", authAdmin().getToken())
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(UsuarioDTO.class).returnResult().getResponseBody();
    }

	protected Autorizacao registrarUsuario(Perfil perfil, String email, String senha){
		UsuarioRequest request = new UsuarioRequest();

		request.setUsername(email);
		request.setNome("Nome " + uuid());
		request.setPassword(senha);
		request.setProfile(perfil.getUuid());
		request.setAtivo(true);

		return this.httpClient.post().uri("/api/v1/register")
				.bodyValue(request)
				.exchange()
				.expectStatus().isEqualTo(201)
				.expectBody(Autorizacao.class).returnResult().getResponseBody();
	}
    
    protected UsuarioDTO criarUsuario(Perfil perfil, String email, String senha){
       return criarUsuario(perfil, email, senha, true);
    }

    
    protected Perfil criarPerfil(List<Permissao> permissoes) {
    	Perfil perfil = new Perfil();
    	perfil.setAtivo(true);
    	perfil.setNome(uuid());
    	perfil.setPermissoes(new HashSet<>());
    	if (permissoes != null) {
    		for (Permissao permissao: permissoes) {
    			perfil.getPermissoes().add(permissao);
    		}
    	}
    	return perfilRepository.save(perfil);
    }

	protected Autorizacao authUser() {
		String email = uuid() + "@teste.com";
		String senha = uuid();
		return this.registrarUsuario(getPerfilAdmin(), email, senha);
	}

	protected Autorizacao authAdmin() {
		return this.autenticar("admin@email.com.br", "admin1234");
	}

	protected Autorizacao autenticar(String usuario, String senha) {
		Credenciais credenciais = new Credenciais(usuario, senha);
		return this.httpClient.post().uri("/api/v1/auth")
				.bodyValue(credenciais)
				.exchange()
				.expectBody(Autorizacao.class).returnResult().getResponseBody();
	}

	protected Random rand() {
		return rand == null ? new SecureRandom() : rand;
	}

	protected TrabalhoResponseDTO criarTrabalho(EmpresaResponseDTO empresaResponseDTO, Autorizacao autorizacao) {
		TrabalhoRequestDTO dto = new TrabalhoRequestDTO();
		dto.setEmpresaUuid(empresaResponseDTO.getUuid());
		dto.setCargo(uuid());
		dto.setEstaTrabalhando(true);
		dto.setDataFim(UtilData.localDateToString(LocalDate.now()));
		dto.setDataInicio(UtilData.localDateToString(LocalDate.now()));

		return this.httpClient.post().uri("/api/v1/trabalho")
				.bodyValue(dto)
				.header("Authorization", autorizacao.getToken())
				.exchange()
				.expectBody(TrabalhoResponseDTO.class).returnResult().getResponseBody();
	}

	protected EmpresaResponseDTO criarEmpresa(Autorizacao autorizacao) {
		EmpresaRequestDTO request = new EmpresaRequestDTO();
		request.setNome(uuid());
		request.setCnpj(uuid());
		request.setDescricao(uuid());
		request.setWebsite(uuid());

		return this.httpClient.post().uri("/api/v1/empresa")
				.bodyValue(request)
				.header("Authorization", autorizacao.getToken())
				.exchange()
				.expectBody(EmpresaResponseDTO.class).returnResult().getResponseBody();
	}

	protected ReviewPublicoEmpresaDTO criarReview(EmpresaResponseDTO empresaResponseDTO, Autorizacao autorizacao) {
		ReviewRequestDTO request = new ReviewRequestDTO();
		request.setEmpresaUuid(empresaResponseDTO.getUuid());
		request.setTitulo(uuid());
		request.setAvaliacaoEmpresa(4);
		request.setComentario(uuid());
		request.setAvaliacaoAmbiente(4);
		request.setAvaliacaoSalario(3);
		request.setAvaliacaoBeneficios(5);
		request.setAvaliacaoGestao(4);

		return this.httpClient.post()
				.uri("/api/v1/review")
				.header("Authorization", autorizacao.getToken())
				.bodyValue(request)
				.exchange()
				.expectStatus().isEqualTo(201)
				.expectBody(ReviewPublicoEmpresaDTO.class)
				.returnResult()
				.getResponseBody();
	}
	
}