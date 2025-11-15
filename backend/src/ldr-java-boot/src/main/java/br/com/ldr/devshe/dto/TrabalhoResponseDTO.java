package br.com.ldr.devshe.dto;

import br.com.ldr.devshe.UtilData;
import br.com.ldr.devshe.domain.Trabalho;

public class TrabalhoResponseDTO {
    private String uuid;
    private String empresaUuid;
    private String empresaNome;
    private String cargo;
    private String dataInicio;
    private String dataFim;
    private Boolean estaTrabalhando;

    public TrabalhoResponseDTO() {}

    public TrabalhoResponseDTO(Trabalho trabalho) {
        this.uuid = trabalho.getUuid();
        this.empresaUuid = trabalho.getEmpresa().getUuid();
        this.empresaNome = trabalho.getEmpresa().getNome();
        this.cargo = trabalho.getCargo();
        if (trabalho.getDataInicio() != null) {
            this.dataInicio = UtilData.localDateToString(trabalho.getDataInicio());
        }
        if (trabalho.getDataFim() != null) {
            this.dataFim = UtilData.localDateToString(trabalho.getDataFim());
        }
        this.estaTrabalhando = trabalho.getEstaTrabalhandoNaEmpresa();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
