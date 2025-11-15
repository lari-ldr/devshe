package br.com.ldr.devshe.dto;

import br.com.ldr.devshe.domain.Usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UsuarioRequest {
	
	@NotNull(message="username indefinido")
	@NotBlank(message="Username em branco")
	private String username;
	
	@NotNull(message="Nome indefinido")
	@NotBlank(message="Nome em branco")
	private String nome;

	private String sobrenome;

	@NotNull(message="Perfil indefinido")
	@NotBlank(message="Perfil em branco")
	private String profile;
	
	@NotNull(message="Deve ser preenchido se o usuário está ativo ou não")
	private boolean ativo;
	
	private String password;
	
	public UsuarioRequest() {
		//construtor padrão
	}
	
	public UsuarioRequest(Usuario usuario) {
		this.username = usuario.getEmail();
		this.profile = usuario.getPerfil().getUuid();
		this.nome = usuario.getNome();
		this.sobrenome = usuario.getSobrenome();
		this.ativo = usuario.isAtivo();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}