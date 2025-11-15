package br.com.ldr.devshe.dto;

import br.com.ldr.devshe.exceptions.ServiceError;

public class Erro {
	
	private String code;
	
	private String message;
	
	public Erro() {
		// construtor padrão
	}
	
	public Erro(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public Erro(ServiceError erro) {
		this.code = "400";
		this.message = erro.getMessage();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
