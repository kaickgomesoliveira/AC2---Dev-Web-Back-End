package com.example.controleprojetos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.controleprojetos.models.Setor;

public interface SetorRepository extends JpaRepository<Setor, Integer> {

    // Consulta d: todos os setores, incluindo os que ainda nao tem funcionarios.
    @Query("""
        select distinct s
        from Setor s
        left join fetch s.funcionarios
        order by s.id
        """)
    List<Setor> buscarTodosComFuncionarios();

    @Query("""
        select distinct s
        from Setor s
        left join fetch s.funcionarios
        where s.id = :id
        """)
    Optional<Setor> buscarSetorComFuncionarios(@Param("id") Integer id);
}