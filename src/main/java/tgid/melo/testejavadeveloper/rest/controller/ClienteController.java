package tgid.melo.testejavadeveloper.rest.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tgid.melo.testejavadeveloper.rest.dto.ClienteDTO;
import tgid.melo.testejavadeveloper.rest.dto.ClienteDetalhadoDTO;
import tgid.melo.testejavadeveloper.rest.dto.TransacaoDTO;
import tgid.melo.testejavadeveloper.service.ClienteService;

@RestController
@RequestMapping("/usuarios")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDetalhadoDTO salvarCliente(@Valid @RequestBody ClienteDTO dto){
        return clienteService.salvarCliente(dto);
    }

    @GetMapping("/{id}")
    public ClienteDetalhadoDTO buscarCliente(@PathVariable("id") int id){
        return clienteService.buscarClienteDetalhado(id);
    }

    @PutMapping("/deposito")
    public ClienteDetalhadoDTO depositar(@Valid @RequestBody TransacaoDTO deposito){
        return clienteService.depositar(deposito);
    }

    @PutMapping("/saque")
    public ClienteDetalhadoDTO sacar(@Valid @RequestBody TransacaoDTO saque){
        return clienteService.sacar(saque);
    }
}
