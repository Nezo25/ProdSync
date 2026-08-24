package br.com.pronova.prodsync.web.controllers;

import br.com.pronova.prodsync.domain.entities.Colaborador;
import br.com.pronova.prodsync.domain.repositories.ColaboradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/colaboradores")
@RequiredArgsConstructor
public class ColaboradorController {

    private final ColaboradorRepository colaboradorRepository;

    @GetMapping
    public ResponseEntity<List<Colaborador>> listarTodos() {
        // Para o MVP, retorna todos. No futuro, filtrar por 'ativo = true'.
        List<Colaborador> lista = colaboradorRepository.findAll();
        return ResponseEntity.ok(lista);
    }
    @org.springframework.web.bind.annotation.PostMapping
    public ResponseEntity<Colaborador> criar(@org.springframework.web.bind.annotation.RequestBody Colaborador colaborador) {
        colaborador.setAtivo(true);
        if (colaborador.getRole() == null) {
            colaborador.setRole(br.com.pronova.prodsync.domain.entities.Role.ROLE_SEPARADOR);
        }
        return ResponseEntity.ok(colaboradorRepository.save(colaborador));
    }
}
