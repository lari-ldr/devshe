package br.com.ldr.devshe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TrabalhoRequestDTO {
    @NotNull
    @NotBlank
    private String empresaUuid;

    @NotNull
    @NotBlank
    private String cargo;

    @NotNull
    @NotBlank
    private String dataInicio;
    private String dataFim;

    @NotNull
    private Boolean estaTrabalhando;

    public String getEmpresaUuid() {
        return empresaUuid;
    }

    public void setEmpresaUuid(String empresaUuid) {
        this.empresaUuid = empresaUuid;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean getEstaTrabalhando() {
        return estaTrabalhando;
    }

    public void setEstaTrabalhando(Boolean estaTrabalhando) {
        this.estaTrabalhando = estaTrabalhando;
    }
}
