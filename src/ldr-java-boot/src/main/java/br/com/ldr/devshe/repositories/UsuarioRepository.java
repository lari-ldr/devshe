package br.com.ldr.devshe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ldr.devshe.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Usuario findByEmail(String email);

	boolean existsByEmail(String email);

	Usuario findByUuid(String uuid);

}
