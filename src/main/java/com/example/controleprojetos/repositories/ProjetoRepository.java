package com.example.controleprojetos.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.controleprojetos.models.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {

    // Consulta a: projeto por ID, incluindo os funcionarios vinculados.
    @Query("""
        select distinct p
        from Projeto p
        left join fetch p.funcionarios f
        left join fetch f.setor
        where p.id = :id
        """)
    Optional<Projeto> buscarProjetoComFuncionarios(@Param("id") Integer id);

    // Consulta b: projetos cuja data de inicio pertence ao intervalo informado.
    @Query("""
        select distinct p
        from Projeto p
        left join fetch p.funcionarios f
        left join fetch f.setor
        where p.dataInicio between :inicio and :fim
        order by p.dataInicio, p.id
        """)
    List<Projeto> buscarPorPeriodo(
        @Param("inicio") LocalDate inicio,
        @Param("fim") LocalDate fim
    );

    @Query("""
        select distinct p
        from Projeto p
        left join fetch p.funcionarios f
        left join fetch f.setor
        order by p.id
        """)
    List<Projeto> buscarTodosComFuncionarios();
}