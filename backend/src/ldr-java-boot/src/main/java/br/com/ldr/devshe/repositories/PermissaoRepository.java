package br.com.ldr.devshe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ldr.devshe.domain.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
	
	Permissao findByAuthority(String authority);

}
