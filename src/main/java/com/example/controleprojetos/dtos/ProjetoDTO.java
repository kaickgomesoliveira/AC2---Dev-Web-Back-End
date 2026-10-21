package com.example.controleprojetos.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoDTO {

    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
}