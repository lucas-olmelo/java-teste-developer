package tgid.melo.testejavadeveloper.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tgid.melo.testejavadeveloper.domain.entity.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
}
