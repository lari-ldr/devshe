package br.com.ldr.devshe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ldr.devshe.domain.AuthLog;

public interface AuthLogRepository extends JpaRepository<AuthLog, Long> {

}
