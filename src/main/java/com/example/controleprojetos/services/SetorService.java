package com.example.controleprojetos.services;

import com.example.controleprojetos.dtos.DadosSetorDTO;
import com.example.controleprojetos.dtos.SetorDTO;
import java.util.List;

public interface SetorService {

    void adicionar(SetorDTO setor);
    DadosSetorDTO buscarSetorPorId(Integer idSetor);
    List<DadosSetorDTO> listar();
}