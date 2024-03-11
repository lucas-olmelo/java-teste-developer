package tgid.melo.testejavadeveloper.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    @NotEmpty(message = "O campo 'nome' deve ser preenchido!")
    private String nome;

    @CPF(message = "CPF inválido")
    @NotEmpty(message = "O campo 'CPF' deve ser preenchido!")
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Column
    private BigDecimal saldo;
}
