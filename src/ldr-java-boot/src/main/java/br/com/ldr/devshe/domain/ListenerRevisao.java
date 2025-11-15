package br.com.ldr.devshe.domain;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.ldr.devshe.security.JWTAuthentication;

public class ListenerRevisao implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		
		Revisao revisao = (Revisao) revisionEntity;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof JWTAuthentication) {
			revisao.setUsuario(((JWTAuthentication) auth).getUsuario());
		}
		
	}

}