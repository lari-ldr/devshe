package br.com.ldr.devshe.dto;

import br.com.ldr.devshe.UtilData;
import br.com.ldr.devshe.domain.Usuario;

public class UsuarioDTO extends UsuarioRequest {
	
	private String uuid;
	private String dateCreated;
	
	public UsuarioDTO() {
		//construtor padrão
	}
	
	public UsuarioDTO(Usuario usuario) {
		super(usuario);
		this.uuid = usuario.getUuid();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

}