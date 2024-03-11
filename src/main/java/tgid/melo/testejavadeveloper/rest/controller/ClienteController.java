package tgid.melo.testejavadeveloper.rest.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tgid.melo.testejavadeveloper.rest.dto.ClienteDTO;
import tgid.melo.testejavadeveloper.rest.dto.ClienteDetalhadoDTO;
import tgid.melo.testejavadeveloper.service.ClienteService;

@RestController
@RequestMapping("/usuarios")
public class ClienteController {

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDetalhadoDTO salvarCliente(@Valid @RequestBody ClienteDTO dto){
        return clienteService.salvarCliente(dto);
    }

    @GetMapping("/{id}")
    public ClienteDetalhadoDTO buscarCliente(@PathVariable("id") int id){
        return clienteService.buscarClienteDetalhado(id);
    }
}
