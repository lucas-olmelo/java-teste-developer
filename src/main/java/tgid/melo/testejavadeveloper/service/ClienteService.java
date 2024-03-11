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

import java.math.BigDecimal;
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
}
