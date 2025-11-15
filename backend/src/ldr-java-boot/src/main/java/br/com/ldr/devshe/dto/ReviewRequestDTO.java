package br.com.ldr.devshe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewRequestDTO {

    @NotNull
    @NotBlank
    private String empresaUuid;

    @NotNull
    @NotBlank
    private String titulo;

    @NotNull
    private Integer avaliacaoEmpresa;

    @NotNull
    @NotBlank
    private String comentario;

    @NotNull
    private Integer avaliacaoAmbiente;

    @NotNull
    private Integer avaliacaoSalario;

    @NotNull
    private Integer avaliacaoBeneficios;

    @NotNull
    private Integer avaliacaoGestao;

    public String getEmpresaUuid() {
        return empresaUuid;
    }

    public void setEmpresaUuid(String empresaUuid) {
        this.empresaUuid = empresaUuid;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAvaliacaoEmpresa() {
        return avaliacaoEmpresa;
    }

    public void setAvaliacaoEmpresa(Integer avaliacaoEmpresa) {
        this.avaliacaoEmpresa = avaliacaoEmpresa;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getAvaliacaoAmbiente() {
        return avaliacaoAmbiente;
    }

    public void setAvaliacaoAmbiente(Integer avaliacaoAmbiente) {
        this.avaliacaoAmbiente = avaliacaoAmbiente;
    }

    public Integer getAvaliacaoSalario() {
        return avaliacaoSalario;
    }

    public void setAvaliacaoSalario(Integer avaliacaoSalario) {
        this.avaliacaoSalario = avaliacaoSalario;
    }

    public Integer getAvaliacaoBeneficios() {
        return avaliacaoBeneficios;
    }

    public void setAvaliacaoBeneficios(Integer avaliacaoBeneficios) {
        this.avaliacaoBeneficios = avaliacaoBeneficios;
    }

    public Integer getAvaliacaoGestao() {
        return avaliacaoGestao;
    }

    public void setAvaliacaoGestao(Integer avaliacaoGestao) {
        this.avaliacaoGestao = avaliacaoGestao;
    }
}
