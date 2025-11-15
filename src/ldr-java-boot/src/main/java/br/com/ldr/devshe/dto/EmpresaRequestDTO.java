package br.com.ldr.devshe.dto;

import br.com.ldr.devshe.domain.Empresa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmpresaRequestDTO {
    @NotNull
    @NotBlank
    private String nome;

    @NotNull
    @NotBlank
    private String cnpj;

    private String descricao;
    private String website;

    public EmpresaRequestDTO() {

    }

    public EmpresaRequestDTO(Empresa empresa) {
        this.nome = empresa.getNome();
        this.cnpj = empresa.getCnpj();
        this.descricao = empresa.getDescricao();
        this.website = empresa.getWebsite();
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
}
