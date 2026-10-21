package com.example.controleprojetos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.controleprojetos.models.Funcionario;
import com.example.controleprojetos.models.Projeto;

public interface FuncionarioRepository
        extends JpaRepository<Funcionario, Integer> {

    // Consulta c: projetos do funcionario, com a lista completa de participantes.
    @Query("""
        select distinct p
        from Funcionario f
        join f.projetos p
        left join fetch p.funcionarios pf
        left join fetch pf.setor
        where f.id = :idFuncionario
        order by p.id
        """)
    List<Projeto> buscarProjetos(
        @Param("idFuncionario") Integer idFuncionario
    );
}