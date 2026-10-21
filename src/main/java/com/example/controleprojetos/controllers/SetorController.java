package com.example.controleprojetos.controllers;

import com.example.controleprojetos.dtos.DadosSetorDTO;
import com.example.controleprojetos.dtos.SetorDTO;
import com.example.controleprojetos.services.SetorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/setores")
@RequiredArgsConstructor
public class SetorController {

    private final SetorService setorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void adicionar(@RequestBody SetorDTO setor) {
        setorService.adicionar(setor);
    }

    @GetMapping("/{idSetor}")
    public DadosSetorDTO buscarSetorPorId(@PathVariable("idSetor") Integer idSetor) {
        return setorService.buscarSetorPorId(idSetor);
    }

    @GetMapping
    public List<DadosSetorDTO> listar() {
        return setorService.listar();
    }
}