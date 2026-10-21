package com.example.controleprojetos.services;

import com.example.controleprojetos.dtos.DadosFuncionarioDTO;
import com.example.controleprojetos.dtos.DadosProjetoDTO;
import com.example.controleprojetos.dtos.ProjetoDTO;
import com.example.controleprojetos.exceptions.RegraNegocioException;
import com.example.controleprojetos.models.Funcionario;
import com.example.controleprojetos.models.Projeto;
import com.example.controleprojetos.repositories.FuncionarioRepository;
import com.example.controleprojetos.repositories.ProjetoRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjetoServiceImpl implements ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final FuncionarioRepository funcionarioRepository;

    @Override
    @Transactional
    public void adicionar(ProjetoDTO dto) {
        if (dto == null || dto.getDescricao() == null || dto.getDescricao().isBlank()) {
            throw new RegraNegocioException("A descrição do projeto é obrigatória.");
        }

        if (dto.getDescricao().trim().length() > 200) {
            throw new RegraNegocioException("A descrição deve ter no máximo 200 caracteres.");
        }

        validarDatas(dto.getDataInicio(), dto.getDataFim());

        Projeto projeto = new Projeto(
            dto.getDescricao().trim(), dto.getDataInicio(), dto.getDataFim()
        );

        projetoRepository.save(projeto);
    }

    @Override
    @Transactional(readOnly = true)
    public DadosProjetoDTO buscarProjetoPorId(Integer id) {
        validarId(id, "projeto");

        Projeto projeto = projetoRepository.buscarProjetoComFuncionarios(id)
            .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado."));

        return converter(projeto);
    }

    @Override
    @Transactional
    public void vincularFuncionario(Integer idProjeto, Integer idFuncionario) {
        validarId(idProjeto, "projeto");
        validarId(idFuncionario, "funcionário");

        Projeto projeto = projetoRepository.buscarProjetoComFuncionarios(idProjeto)
            .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado."));

        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
            .orElseThrow(() -> new RegraNegocioException("Funcionário não encontrado."));

        boolean jaVinculado = projeto.getFuncionarios().stream()
            .anyMatch(f -> f.getId().equals(idFuncionario));

        if (jaVinculado) {
            throw new RegraNegocioException("Funcionário já vinculado ao projeto.");
        }

        // Projeto é o lado que possui o mapeamento da tabela de vínculo.
        projeto.getFuncionarios().add(funcionario);
        projetoRepository.save(projeto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> listar() {
        return projetoRepository.buscarTodosComFuncionarios().stream()
            .map(this::converter)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        validarDatas(inicio, fim);

        return projetoRepository.buscarPorPeriodo(inicio, fim).stream()
            .map(this::converter)
            .collect(Collectors.toList());
    }

    private void validarId(Integer id, String entidade) {
        if (id == null || id <= 0) {
            throw new RegraNegocioException("Informe um ID de " + entidade + " maior que zero.");
        }
    }

    private void validarDatas(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new RegraNegocioException("As datas de início e fim são obrigatórias.");
        }

        if (fim.isBefore(inicio)) {
            throw new RegraNegocioException("A data de fim não pode ser anterior à data de início.");
        }
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