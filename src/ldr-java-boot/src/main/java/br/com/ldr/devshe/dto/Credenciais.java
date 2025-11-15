package br.com.ldr.devshe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Credenciais {

	@NotNull
	@NotBlank
	private String username;

	@NotNull
	@NotBlank
	private String password;
	
	public Credenciais() {
		// construtor padrão
	}
	
	public Credenciais(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
