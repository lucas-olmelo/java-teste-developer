package tgid.melo.testejavadeveloper.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tgid.melo.testejavadeveloper.domain.entity.Cliente;
import tgid.melo.testejavadeveloper.domain.entity.Empresa;
import tgid.melo.testejavadeveloper.domain.repository.ClienteRepository;
import tgid.melo.testejavadeveloper.domain.repository.EmpresaRepository;
import tgid.melo.testejavadeveloper.rest.dto.ClienteDTO;
import tgid.melo.testejavadeveloper.rest.dto.ClienteDetalhadoDTO;
import tgid.melo.testejavadeveloper.rest.dto.TransacaoDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

@Service
public class ClienteService {

    public ClienteService(ClienteRepository clienteRepository, EmpresaRepository empresaRepository, EmailService emailService) {
        this.clienteRepository = clienteRepository;
        this.empresaRepository = empresaRepository;
        this.emailService = emailService;
    }

    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    private final EmailService emailService;

    @Transactional
    public ClienteDetalhadoDTO salvarCliente(ClienteDTO clienteDTO){
        int idEmpresa = clienteDTO.getEmpresa();

        Optional<Empresa> empresa = empresaRepository.findById(idEmpresa);
        if (empresa.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada!");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.getNome());
        cliente.setCpf(clienteDTO.getCpf());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setEmpresa(empresa.get());
        cliente.setSaldo(BigDecimal.valueOf(0));

        clienteRepository.save(cliente);

        return buscarClienteDetalhado(cliente.getId());
    }

    public ClienteDetalhadoDTO depositar(TransacaoDTO deposito){
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

                emailService.sendMail(
                        cliente.getEmail(),
                        criarTituloConteudoCliente(empresa.getNome(), valorTotal, taxaEmpresa, "Depósito")[0],
                        criarTituloConteudoCliente(empresa.getNome(), valorTotal, taxaEmpresa, "Depósito")[1]
                );

                emailService.sendMail(
                        empresa.getEmail(),
                        criarTituloConteudoEmpresa(cliente.getNome(), valorTotal, taxaEmpresa, "Depósito")[0],
                        criarTituloConteudoEmpresa(cliente.getNome(), valorTotal, taxaEmpresa, "Depósito")[1]
                );

                return buscarClienteDetalhado(cliente.getId());
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrada!"));
    }

    public ClienteDetalhadoDTO sacar(TransacaoDTO saque){
        return clienteRepository
                .findById(saque.getCliente())
                .map(cliente -> {
                    Empresa empresa = cliente.getEmpresa();
                    float taxaEmpresa = empresa.getTaxa();

                    BigDecimal saqueTotal = saque.getValor();
                    BigDecimal saqueCorrigido = saqueTotal.multiply(BigDecimal.valueOf((100 + taxaEmpresa) / 100));

                    if (empresa.getSaldo().compareTo(saqueTotal) >= 0 && cliente.getSaldo().compareTo(saqueTotal) >= 0){

                        empresa.setSaldo(empresa.getSaldo().subtract(saqueCorrigido).setScale(2, RoundingMode.HALF_DOWN));
                        cliente.setSaldo(cliente.getSaldo().subtract(saqueTotal).setScale(2, RoundingMode.HALF_DOWN));

                        empresaRepository.save(empresa);
                        clienteRepository.save(cliente);

                        emailService.sendMail(
                                cliente.getEmail(),
                                criarTituloConteudoCliente(empresa.getNome(), saqueTotal, taxaEmpresa, "Saque")[0],
                                criarTituloConteudoCliente(empresa.getNome(), saqueTotal, taxaEmpresa, "Saque")[1]
                        );

                        emailService.sendMail(
                                empresa.getEmail(),
                                criarTituloConteudoEmpresa(cliente.getNome(), saqueTotal, taxaEmpresa, "Saque")[0],
                                criarTituloConteudoEmpresa(cliente.getNome(), saqueTotal, taxaEmpresa, "Saque")[1]
                        );

                        return buscarClienteDetalhado(cliente.getId());
                    } else {
                        throw new RuntimeException("Saldo insuficiente para saque");
                    }

                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrada!"));
    }

    public ClienteDetalhadoDTO buscarClienteDetalhado(int id){
        return clienteRepository
                .findById(id)
                .map(cliente -> {
                    ClienteDetalhadoDTO clienteDetalhado = new ClienteDetalhadoDTO();
                    clienteDetalhado.setNome(cliente.getNome());
                    clienteDetalhado.setCpf(cliente.getCpf());
                    clienteDetalhado.setEmail(cliente.getEmail());
                    clienteDetalhado.setEmpresa(cliente.getEmpresa().getNome());
                    clienteDetalhado.setSaldo(cliente.getSaldo());
                    return clienteDetalhado;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrada!"));
    }

    public String[] criarTituloConteudoCliente(String nomeEmpresa, BigDecimal valor, float taxa, String tipo) {
        String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        Locale br = new Locale.Builder().setLanguage("pt").setRegion("BR").build();

        String assunto = tipo + " - " + dataAtual;
        String conteudo = tipo + " realizado na sua conta " + nomeEmpresa +
                "\nValor: " + NumberFormat.getCurrencyInstance(br).format(valor) +
                "\nData e hora: " + dataAtual;

        String[] resposta = new String[2];
        resposta[0] = assunto;
        resposta[1] = conteudo;
        return resposta;
    }

    public String[] criarTituloConteudoEmpresa(String nomeCliente, BigDecimal valor, float taxa, String tipo) {
        String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        Locale br = new Locale.Builder().setLanguage("pt").setRegion("BR").build();

        String assunto = tipo + " - " + dataAtual;
        String conteudo = "O cliente " + nomeCliente + " realizou um " + tipo +
                " no valor de " + NumberFormat.getCurrencyInstance(br).format(valor) +
                "\nTaxa de sistema: " + taxa + "%" +
                "\nData e hora: " + dataAtual;

        String[] resposta = new String[2];
        resposta[0] = assunto;
        resposta[1] = conteudo;
        return resposta;
    }
}
