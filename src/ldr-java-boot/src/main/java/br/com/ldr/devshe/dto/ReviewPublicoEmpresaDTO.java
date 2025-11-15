package br.com.ldr.devshe.dto;

import br.com.ldr.devshe.domain.Review;

import java.time.LocalDateTime;

public class ReviewPublicoEmpresaDTO {

    private String uuid;
    private String titulo;
    private Integer avaliacaoEmpresa;
    private String comentario;
    private Integer avaliacaoAmbiente;
    private Integer avaliacaoSalario;
    private Integer avaliacaoBeneficios;
    private Integer avaliacaoGestao;
    private LocalDateTime dataCriacao;
    private String empresaUuid;
    private String empresaNome;

    public ReviewPublicoEmpresaDTO() {}

    public ReviewPublicoEmpresaDTO(Review review) {
        this.uuid = review.getUuid();
        this.titulo = review.getTitulo();
        this.avaliacaoEmpresa = review.getAvaliacaoEmpresa();
        this.comentario = review.getComentario();
        this.avaliacaoAmbiente = review.getAvaliacaoAmbiente();
        this.avaliacaoSalario = review.getAvaliacaoSalario();
        this.avaliacaoBeneficios = review.getAvaliacaoBeneficios();
        this.avaliacaoGestao = review.getAvaliacaoGestao();
        this.dataCriacao = review.getCreatedAt();
        this.empresaUuid = review.getEmpresa().getUuid();
        this.empresaNome = review.getEmpresa().getNome();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getEmpresaUuid() {
        return empresaUuid;
    }

    public void setEmpresaUuid(String empresaUuid) {
        this.empresaUuid = empresaUuid;
    }

    public String getEmpresaNome() {
        return empresaNome;
    }

    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }
}
