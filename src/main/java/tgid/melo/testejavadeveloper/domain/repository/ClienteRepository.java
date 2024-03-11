package tgid.melo.testejavadeveloper.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tgid.melo.testejavadeveloper.domain.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
