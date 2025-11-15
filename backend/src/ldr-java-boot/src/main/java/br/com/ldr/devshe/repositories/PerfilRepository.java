package br.com.ldr.devshe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ldr.devshe.domain.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
	
	Perfil findByNome(String nome);
	
	Perfil findByUuid(String uuid);

}
