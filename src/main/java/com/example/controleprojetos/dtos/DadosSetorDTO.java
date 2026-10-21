package com.example.controleprojetos.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DadosSetorDTO {

    private Integer id;
    private String nome;
    private List<DadosFuncionarioDTO> funcionarios;
}