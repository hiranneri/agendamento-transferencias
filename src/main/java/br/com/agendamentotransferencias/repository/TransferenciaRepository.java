package br.com.agendamentotransferencias.repository;

import br.com.agendamentotransferencias.model.Transferencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    Page<Transferencia> findAllByOrderByDataAgendamentoDesc(Pageable pageable);

}
