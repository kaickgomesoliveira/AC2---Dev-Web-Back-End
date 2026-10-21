package com.example.controleprojetos.services;

import com.example.controleprojetos.dtos.DadosProjetoDTO;
import com.example.controleprojetos.dtos.FuncionarioDTO;
import java.util.List;

public interface FuncionarioService {

    void adicionar(FuncionarioDTO funcionario);
    List<DadosProjetoDTO> buscarProjetos(Integer idFuncionario);
}