package tgid.melo.testejavadeveloper.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDetalhadoDTO {
    private String nome;
    private String cpf;
    private String email;
    private String empresa;
    private BigDecimal saldo;
}
