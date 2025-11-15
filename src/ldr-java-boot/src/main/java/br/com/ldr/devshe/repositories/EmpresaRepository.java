package br.com.ldr.devshe.repositories;

import br.com.ldr.devshe.domain.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByCnpj(String cnpj);
    Optional<Empresa> findByUuid(String uuid);
}
