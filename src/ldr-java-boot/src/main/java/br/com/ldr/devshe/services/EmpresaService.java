package br.com.ldr.devshe.services;

import br.com.ldr.devshe.domain.Empresa;
import br.com.ldr.devshe.domain.Trabalho;
import br.com.ldr.devshe.domain.Usuario;
import br.com.ldr.devshe.dto.EmpresaRequestDTO;
import br.com.ldr.devshe.dto.EmpresaResponseDTO;
import br.com.ldr.devshe.dto.TrabalhoRequestDTO;
import br.com.ldr.devshe.dto.TrabalhoResponseDTO;
import br.com.ldr.devshe.exceptions.NotFoundError;
import br.com.ldr.devshe.exceptions.ServiceError;
import br.com.ldr.devshe.repositories.EmpresaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    private final EntityManager entityManager;

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EntityManager entityManager, EmpresaRepository empresaRepository) {
        this.entityManager = entityManager;
        this.empresaRepository = empresaRepository;
    }

    @Transactional
    public EmpresaResponseDTO criarEmpresa(EmpresaRequestDTO request) throws NotFoundError {
        Optional<Empresa> empresaExistsByDoc = empresaRepository.findByCnpj(request.getCnpj());
        if (empresaExistsByDoc.isPresent()) {
            throw new ServiceError("Empresa já existe!");
        }

        Empresa empresa = new Empresa(request);
        empresa = empresaRepository.save(empresa);
        return new EmpresaResponseDTO(empresa);
    }

    @Transactional(readOnly = true)
    public Empresa getEmpresaByUuid(String uuid) throws NotFoundError {
        Optional<Empresa> empresaOptional = empresaRepository.findByUuid(uuid);

        if (empresaOptional.isEmpty()) {
            throw new NotFoundError("Empresa não encontrada");
        }

        return empresaOptional.get();
    }

    @Transactional(readOnly = true)
    public List<Empresa> pesquisarEmpresas(String nome) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresa> query = builder.createQuery(Empresa.class);
        Root<Empresa> root = query.from(Empresa.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (nome != null) {
            predicates.add(builder.like(builder.lower(root.get("nome")), "%" + nome.toLowerCase().trim()+"%"));
        }

        query.where(predicates.toArray(new Predicate[] {}));

        TypedQuery<Empresa> queryFinal = this.entityManager.createQuery(query);
        return queryFinal.getResultList().isEmpty() ? new ArrayList<>() : queryFinal.getResultList();
    }
}
