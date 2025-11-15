package br.com.ldr.devshe.repositories;

import br.com.ldr.devshe.domain.Empresa;
import br.com.ldr.devshe.domain.Trabalho;
import br.com.ldr.devshe.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrabalhoRepository extends JpaRepository<Trabalho, Long> {
    List<Trabalho> findAllByUsuario(Usuario usuario);
    List<Trabalho> findAllByEmpresa(Empresa empresa);

    boolean existsByUsuarioAndEmpresa(Usuario usuario, Empresa empresa);
    Optional<Trabalho> findByUsuarioAndEmpresa(Usuario usuario, Empresa empresa);
}
