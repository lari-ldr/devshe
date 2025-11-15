package br.com.ldr.devshe.domain;

import br.com.ldr.devshe.dto.EmpresaRequestDTO;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.Set;

@Audited
@Entity
@Table(name="empresa")
public class Empresa extends BaseDomain {

    @Column(name="nome", nullable = false, unique=false, length=128)
    private String nome;

    @Column(name="cnpj", unique = true, nullable = false)
    private String cnpj;

    @Column(name="descricao", nullable = true)
    private String descricao;

    @Column(name="website", nullable = true)
    private String website;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Trabalho> trabalhos = new HashSet<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    public Empresa() {}

    public Empresa(EmpresaRequestDTO empresaRequestDTO) {
        this.nome = empresaRequestDTO.getNome();
        this.cnpj = empresaRequestDTO.getCnpj();
        if (empresaRequestDTO.getDescricao() != null) {
            this.descricao = empresaRequestDTO.getDescricao();
        }
        if (empresaRequestDTO.getWebsite() != null) {
            this.website = empresaRequestDTO.getWebsite();
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Set<Trabalho> getTrabalhos() {
        return trabalhos;
    }

    public void setTrabalhos(Set<Trabalho> trabalhos) {
        this.trabalhos = trabalhos;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }
}
