package tgid.melo.testejavadeveloper.rest.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tgid.melo.testejavadeveloper.rest.dto.ClienteDetalhadoDTO;
import tgid.melo.testejavadeveloper.rest.dto.TransacaoDTO;
import tgid.melo.testejavadeveloper.service.TransacaoService;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }
    private final TransacaoService transacaoService;

    @PutMapping("/deposito")
    public ClienteDetalhadoDTO depositar(@Valid @RequestBody TransacaoDTO deposito){
        return transacaoService.depositar(deposito);
    }

    @PutMapping("/saque")
    public ClienteDetalhadoDTO sacar(@Valid @RequestBody TransacaoDTO saque) {
        return transacaoService.sacar(saque);
    }
}
