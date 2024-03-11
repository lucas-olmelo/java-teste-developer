package tgid.melo.testejavadeveloper.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoDTO {
    private int cliente;
    private BigDecimal valor;
}
