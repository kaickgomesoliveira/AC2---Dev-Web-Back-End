package com.example.controleprojetos.services;

import com.example.controleprojetos.dtos.DadosProjetoDTO;
import com.example.controleprojetos.dtos.ProjetoDTO;
import java.time.LocalDate;
import java.util.List;

public interface ProjetoService {

    void adicionar(ProjetoDTO projeto);
    DadosProjetoDTO buscarProjetoPorId(Integer id);
    void vincularFuncionario(Integer idProjeto, Integer idFuncionario);
    List<DadosProjetoDTO> listar();
    List<DadosProjetoDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim);
}