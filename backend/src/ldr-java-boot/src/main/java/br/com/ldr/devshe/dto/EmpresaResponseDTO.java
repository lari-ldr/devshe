package br.com.ldr.devshe.dto;

import br.com.ldr.devshe.domain.Empresa;

public class EmpresaResponseDTO {
    private String uuid;
    private String nome;
    private String cnpj;
    private String descricao;
    private String website;

    public EmpresaResponseDTO() {

    }

    public EmpresaResponseDTO(Empresa empresa) {
        this.uuid = empresa.getUuid();
        this.nome = empresa.getNome();
        this.cnpj = empresa.getCnpj();
        this.descricao = empresa.getDescricao();
        this.website = empresa.getWebsite();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
