package br.com.ldr.devshe.repositories;

import br.com.ldr.devshe.domain.Empresa;
import br.com.ldr.devshe.domain.Review;
import br.com.ldr.devshe.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByEmpresaAndAtivoTrue(Empresa empresa);

    Optional<Review> findByAutorAndEmpresaAndAtivoTrue(Usuario autor, Empresa empresa);

    Optional<Review> findByUuidAndAutorAndEmpresaAndAtivoTrue(String uuid, Usuario autor, Empresa empresa);

    Optional<Review> findByUuidAndAutorAndAtivoTrue(String uuid, Usuario autor);
    Optional<Review> findByUuid(String uuid);

    boolean existsByAutorAndEmpresa(Usuario autor, Empresa empresa);
}
