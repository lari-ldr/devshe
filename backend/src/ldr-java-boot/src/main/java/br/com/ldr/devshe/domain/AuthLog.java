package br.com.ldr.devshe.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity @Table(name="auth_log")
public class AuthLog {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="momento", nullable=false)
	private Date momento;
	
	@ManyToOne @JoinColumn(name="usuario_id")
	private Usuario usuario;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getMomento() {
		return momento;
	}

	public void setMomento(Date momento) {
		this.momento = momento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	

}
