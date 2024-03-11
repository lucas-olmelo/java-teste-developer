package tgid.melo.testejavadeveloper.rest.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tgid.melo.testejavadeveloper.domain.entity.Empresa;
import tgid.melo.testejavadeveloper.domain.repository.EmpresaRepository;

import java.math.BigDecimal;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Empresa salvarEmpresa(@Valid @RequestBody Empresa empresa){
        empresa.setSaldo(BigDecimal.valueOf(0));
        return repository.save(empresa);
    }

    @GetMapping("/{id}")
    public Empresa buscarEmpresa(@PathVariable int id){
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }
}
