package com.example.controleprojetos.services;

import com.example.controleprojetos.dtos.DadosFuncionarioDTO;
import com.example.controleprojetos.dtos.DadosSetorDTO;
import com.example.controleprojetos.dtos.SetorDTO;
import com.example.controleprojetos.exceptions.RegraNegocioException;
import com.example.controleprojetos.models.Funcionario;
import com.example.controleprojetos.models.Setor;
import com.example.controleprojetos.repositories.SetorRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SetorServiceImpl implements SetorService {

    private final SetorRepository setorRepository;

    @Override
    @Transactional
    public void adicionar(SetorDTO dto) {
        if (dto == null || dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RegraNegocioException("O nome do setor é obrigatório.");
        }

        if (dto.getNome().trim().length() > 200) {
            throw new RegraNegocioException("O nome deve ter no máximo 200 caracteres.");
        }

        setorRepository.save(new Setor(dto.getNome().trim()));
    }

    @Override
    @Transactional(readOnly = true)
    public DadosSetorDTO buscarSetorPorId(Integer idSetor) {
        if (idSetor == null || idSetor <= 0) {
            throw new RegraNegocioException("Informe um ID de setor maior que zero.");
        }

        Setor setor = setorRepository.buscarSetorComFuncionarios(idSetor)
            .orElseThrow(() -> new RegraNegocioException("Setor não encontrado."));

        return converter(setor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DadosSetorDTO> listar() {
        return setorRepository.buscarTodosComFuncionarios().stream()
            .map(this::converter)
            .collect(Collectors.toList());
    }

    private DadosSetorDTO converter(Setor setor) {
        List<DadosFuncionarioDTO> funcionarios = setor.getFuncionarios().stream()
            .sorted(Comparator.comparing(Funcionario::getId))
            .map(f -> DadosFuncionarioDTO.builder()
                .id(f.getId())
                .nome(f.getNome())
                .setorId(setor.getId())
                .setorNome(setor.getNome())
                .build())
            .collect(Collectors.toList());

        return DadosSetorDTO.builder()
            .id(setor.getId())
            .nome(setor.getNome())
            .funcionarios(funcionarios)
            .build();
    }
}