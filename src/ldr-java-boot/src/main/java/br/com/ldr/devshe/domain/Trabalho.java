package br.com.ldr.devshe.domain;

import br.com.ldr.devshe.UtilData;
import br.com.ldr.devshe.dto.TrabalhoRequestDTO;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Audited
@Entity
@Table(name="trabalho")
public class Trabalho extends BaseDomain{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "esta_trabalhando_empresa", nullable = false)
    private Boolean estaTrabalhandoNaEmpresa;

    public Trabalho() {}

    public Trabalho(TrabalhoRequestDTO request, Usuario usuario, Empresa empresa) {
        this.usuario = usuario;
        this.empresa = empresa;
        this.cargo = request.getCargo();
        if (request.getDataInicio() != null) {
            this.dataInicio = UtilData.stringToLocalDate(request.getDataInicio());
        }
        if (request.getDataFim() != null) {
            this.dataFim = UtilData.stringToLocalDate(request.getDataFim());
        }
        this.estaTrabalhandoNaEmpresa = request.getEstaTrabalhando();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean getEstaTrabalhandoNaEmpresa() {
        return estaTrabalhandoNaEmpresa;
    }

    public void setEstaTrabalhandoNaEmpresa(Boolean estaTrabalhandoNaEmpresa) {
        this.estaTrabalhandoNaEmpresa = estaTrabalhandoNaEmpresa;
    }
}
