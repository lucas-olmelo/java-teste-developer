package tgid.melo.testejavadeveloper.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tgid.melo.testejavadeveloper.domain.entity.Empresa;
import tgid.melo.testejavadeveloper.domain.repository.ClienteRepository;
import tgid.melo.testejavadeveloper.domain.repository.EmpresaRepository;
import tgid.melo.testejavadeveloper.rest.dto.ClienteDetalhadoDTO;
import tgid.melo.testejavadeveloper.rest.dto.TransacaoDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class TransacaoService {

    public TransacaoService(
            ClienteRepository clienteRepository,
            EmpresaRepository empresaRepository,
            EmailService emailService,
            ClienteService clienteService) {
        this.clienteRepository = clienteRepository;
        this.empresaRepository = empresaRepository;
        this.emailService = emailService;
        this.clienteService = clienteService;
    }

    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    private final EmailService emailService;
    private final ClienteService clienteService;

    public ClienteDetalhadoDTO depositar(TransacaoDTO deposito) {
        return clienteRepository
                .findById(deposito.getCliente())
                .map(cliente -> {
                    Empresa empresa = cliente.getEmpresa();
                    float taxaEmpresa = empresa.getTaxa();

                    BigDecimal valorTotal = deposito.getValor();
                    BigDecimal valorCorrigido = valorTotal.multiply(BigDecimal.valueOf((100 - taxaEmpresa) / 100));

                    empresa.setSaldo(empresa.getSaldo().add(valorCorrigido).setScale(2, RoundingMode.HALF_DOWN));
                    cliente.setSaldo(cliente.getSaldo().add(valorTotal).setScale(2, RoundingMode.HALF_DOWN));

                    empresaRepository.save(empresa);
                    clienteRepository.save(cliente);

                    String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                    String conteudoEmail =
                            criarTituloConteudo(cliente.getNome(), empresa.getNome(), valorTotal, empresa.getTaxa(), "Depósito", dataAtual);

                    emailService.sendMail(cliente.getEmail(), conteudoEmail.split("::")[0], conteudoEmail.split("::")[1]);
                    emailService.sendMail(empresa.getEmail(), conteudoEmail.split("::")[0], conteudoEmail.split("::")[2]);

                    return clienteService.buscarClienteDetalhado(cliente.getId());
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrada!"));
    }

    public ClienteDetalhadoDTO sacar(TransacaoDTO saque) {
        return clienteRepository
                .findById(saque.getCliente())
                .map(cliente -> {
                    Empresa empresa = cliente.getEmpresa();
                    float taxaEmpresa = empresa.getTaxa();

                    BigDecimal saqueTotal = saque.getValor();
                    BigDecimal saqueCorrigido = saqueTotal.multiply(BigDecimal.valueOf((100 + taxaEmpresa) / 100));

                    if (empresa.getSaldo().compareTo(saqueTotal) >= 0 && cliente.getSaldo().compareTo(saqueTotal) >= 0) {

                        empresa.setSaldo(empresa.getSaldo().subtract(saqueCorrigido).setScale(2, RoundingMode.HALF_DOWN));
                        cliente.setSaldo(cliente.getSaldo().subtract(saqueTotal).setScale(2, RoundingMode.HALF_DOWN));

                        empresaRepository.save(empresa);
                        clienteRepository.save(cliente);

                        String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                        String conteudoEmail =
                                criarTituloConteudo(cliente.getNome(), empresa.getNome(), saqueTotal, empresa.getTaxa(), "Saque", dataAtual);

                        emailService.sendMail(cliente.getEmail(), conteudoEmail.split("::")[0], conteudoEmail.split("::")[1]);
                        emailService.sendMail(empresa.getEmail(), conteudoEmail.split("::")[0], conteudoEmail.split("::")[2]);

                        return clienteService.buscarClienteDetalhado(cliente.getId());
                    } else {
                        throw new RuntimeException("Saldo insuficiente para saque");
                    }

                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrada!"));
    }

    public String criarTituloConteudo(String nomeCliente, String nomeEmpresa, BigDecimal valor, float taxa, String tipo, String dataAtual) {
        Locale br = new Locale.Builder().setLanguage("pt").setRegion("BR").build();

        String assunto = tipo + " - " + dataAtual;

        String conteudoCliente = tipo + " realizado na sua conta " + nomeEmpresa +
                "\nValor: " + NumberFormat.getCurrencyInstance(br).format(valor) +
                "\nData e hora: " + dataAtual;

        String conteudoEmpresa = "O cliente " + nomeCliente + " realizou um " + tipo +
                " no valor de " + NumberFormat.getCurrencyInstance(br).format(valor) +
                "\nTaxa de sistema: " + taxa + "%" +
                "\nData e hora: " + dataAtual;

        return assunto + "::" + conteudoCliente + "::" + conteudoEmpresa;
    }
}