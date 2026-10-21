package com.example.controleprojetos.services;

import com.example.controleprojetos.dtos.DadosFuncionarioDTO;
import com.example.controleprojetos.dtos.DadosProjetoDTO;
import com.example.controleprojetos.dtos.FuncionarioDTO;
import com.example.controleprojetos.exceptions.RegraNegocioException;
import com.example.controleprojetos.models.Funcionario;
import com.example.controleprojetos.models.Projeto;
import com.example.controleprojetos.models.Setor;
import com.example.controleprojetos.repositories.FuncionarioRepository;
import com.example.controleprojetos.repositories.SetorRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FuncionarioServiceImpl implements FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final SetorRepository setorRepository;

    @Override
    @Transactional
    public void adicionar(FuncionarioDTO dto) {
        if (dto == null || dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RegraNegocioException("O nome do funcionário é obrigatório.");
        }

        if (dto.getNome().trim().length() > 200) {
            throw new RegraNegocioException("O nome deve ter no máximo 200 caracteres.");
        }

        if (dto.getSetorId() == null || dto.getSetorId() <= 0) {
            throw new RegraNegocioException("Informe um setorId maior que zero.");
        }

        Setor setor = setorRepository.findById(dto.getSetorId())
            .orElseThrow(() -> new RegraNegocioException("Setor não encontrado."));

        Funcionario funcionario = new Funcionario(dto.getNome().trim());
        funcionario.setSetor(setor);

        funcionarioRepository.save(funcionario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> buscarProjetos(Integer idFuncionario) {
        if (idFuncionario == null || idFuncionario <= 0) {
            throw new RegraNegocioException("Informe um ID de funcionário maior que zero.");
        }

        if (!funcionarioRepository.existsById(idFuncionario)) {
            throw new RegraNegocioException("Funcionário não encontrado.");
        }

        return funcionarioRepository.buscarProjetos(idFuncionario).stream()
            .map(this::converter)
            .collect(Collectors.toList());
    }

    private DadosProjetoDTO converter(Projeto projeto) {
        List<DadosFuncionarioDTO> funcionarios = projeto.getFuncionarios().stream()
            .sorted(Comparator.comparing(Funcionario::getId))
            .map(f -> DadosFuncionarioDTO.builder()
                .id(f.getId())
                .nome(f.getNome())
                .setorId(f.getSetor().getId())
                .setorNome(f.getSetor().getNome())
                .build())
            .collect(Collectors.toList());

        return DadosProjetoDTO.builder()
            .id(projeto.getId())
            .descricao(projeto.getDescricao())
            .dataInicio(projeto.getDataInicio())
            .dataFim(projeto.getDataFim())
            .funcionarios(funcionarios)
            .build();
    }
}