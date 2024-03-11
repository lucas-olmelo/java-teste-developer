package tgid.melo.testejavadeveloper.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    @NotEmpty(message = "O campo 'nome' precisa ser preenchido!")
    private String nome;

    @CNPJ(message = "CNPJ inválido!")
    @Column
    @NotEmpty(message = "O campo 'CNPJ' precisa ser preenchido!")
    private String cnpj;

    @Column
    @NotEmpty(message = "O campo 'email' deve ser preenchido!")
    private String email;

    @Column
    @NotNull(message = "O campo 'taxa' precisa ser preenchido!")
    private float taxa;

    @Column
    private BigDecimal saldo;

    @JsonIgnore
    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY)
    private Set<Cliente> clientes;

}
