package com.example.controleprojetos.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DadosFuncionarioDTO {

    private Integer id;
    private String nome;
    private Integer setorId;
    private String setorNome;
}