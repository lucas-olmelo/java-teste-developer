package tgid.melo.testejavadeveloper.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    @NotEmpty(message = "O campo nome não pode ser vazio!")
    private String nome;

    @CPF(message = "CPF inválido!")
    @NotEmpty(message = "O campo cpf não pode ser vazio!")
    private String cpf;

    @NotNull(message = "O campo empresa não pode ser vazio!")
    private int empresa;
}
