package br.com.ldr.devshe.services;

import br.com.ldr.devshe.domain.Empresa;
import br.com.ldr.devshe.domain.Trabalho;
import br.com.ldr.devshe.domain.Usuario;
import br.com.ldr.devshe.dto.TrabalhoRequestDTO;
import br.com.ldr.devshe.dto.TrabalhoResponseDTO;
import br.com.ldr.devshe.exceptions.NotFoundError;
import br.com.ldr.devshe.exceptions.ServiceError;
import br.com.ldr.devshe.repositories.TrabalhoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrabalhoService {

    private final TrabalhoRepository trabalhoRepository;
    private final EmpresaService empresaService;

    public TrabalhoService(TrabalhoRepository trabalhoRepository, EmpresaService empresaService) {
        this.trabalhoRepository = trabalhoRepository;
        this.empresaService = empresaService;
    }

    @Transactional
    public TrabalhoResponseDTO criarTrabalho(Usuario usuario, TrabalhoRequestDTO request) throws NotFoundError {
        Empresa empresa = empresaService.getEmpresaByUuid(request.getEmpresaUuid());
        if (trabalhoRepository.existsByUsuarioAndEmpresa(usuario, empresa)) {
            throw new ServiceError("Usuário já possui registro de trabalho nessa empresa");
        }

        Trabalho trabalho = new Trabalho(request, usuario, empresa);
        trabalho = trabalhoRepository.save(trabalho);
        return new TrabalhoResponseDTO(trabalho);
    }

    @Transactional(readOnly = true)
    public List<TrabalhoResponseDTO> listarTrabalhosPorUsuario(Usuario usuario) {
        List<TrabalhoResponseDTO> trabalhoResponseDTOList = new ArrayList<>();
        List<Trabalho> trabalhos = trabalhoRepository.findAllByUsuario(usuario);
        if (trabalhos.isEmpty()) return trabalhoResponseDTOList;
        trabalhos.forEach(trabalho -> {
            trabalhoResponseDTOList.add(new TrabalhoResponseDTO(trabalho));
        });
        return trabalhoResponseDTOList;
    }
}
