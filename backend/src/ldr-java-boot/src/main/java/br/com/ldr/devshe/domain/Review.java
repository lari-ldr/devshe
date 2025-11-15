package br.com.ldr.devshe.domain;

import br.com.ldr.devshe.dto.ReviewRequestDTO;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name="review", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"autor_id", "empresa_id"})
})
public class Review extends BaseDomain {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name="avaliacao_empresa", nullable = false)
    private Integer avaliacaoEmpresa; // 1 a 5 estrelas

    @Column(name = "cometario", nullable = false)
    private String comentario;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "avaliacao_ambiente", nullable = false)
    private Integer avaliacaoAmbiente;

    @Column(name = "avaliacao_salario", nullable = false)
    private Integer avaliacaoSalario;

    @Column(name = "avaliacao_beneficios", nullable = false)
    private Integer avaliacaoBeneficios;

    @Column(name = "avaliacao_gestao", nullable = false)
    private Integer avaliacaoGestao;

    public Review() {

    }

    public Review(ReviewRequestDTO requestDTO, Usuario usuario, Empresa empresa) {
        this.autor = usuario;
        this.empresa = empresa;
        this.titulo = requestDTO.getTitulo();
        this.avaliacaoEmpresa = requestDTO.getAvaliacaoEmpresa();
        this.comentario = requestDTO.getComentario();
        this.avaliacaoAmbiente = requestDTO.getAvaliacaoAmbiente();
        this.avaliacaoSalario = requestDTO.getAvaliacaoSalario();
        this.avaliacaoBeneficios = requestDTO.getAvaliacaoBeneficios();
        this.avaliacaoGestao = requestDTO.getAvaliacaoGestao();
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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
