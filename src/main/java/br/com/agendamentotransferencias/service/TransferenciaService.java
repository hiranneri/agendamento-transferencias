package br.com.agendamentotransferencias.service;


import br.com.agendamentotransferencias.controller.dto.TransferenciaDTO;
import br.com.agendamentotransferencias.controller.dto.TransferenciaResponseDTO;
import br.com.agendamentotransferencias.model.Transferencia;
import br.com.agendamentotransferencias.repository.TransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @Autowired
    private CalculadoraTaxaService calculadoraTaxaService;


    @Transactional
    public TransferenciaResponseDTO agendarTransferencia(TransferenciaDTO dto) {
        LocalDate dataAgendamento = LocalDate.now();

        BigDecimal taxa = calculadoraTaxaService.calcular(
                new CalculoTaxaDTO(dataAgendamento, dto.getDataTransferencia(), dto.getValorTransferencia())
        );

        Transferencia transferencia = new Transferencia();
        transferencia.setContaOrigem(dto.getContaOrigem());
        transferencia.setContaDestino(dto.getContaDestino());
        transferencia.setValorTransferencia(dto.getValorTransferencia());
        transferencia.setTaxa(taxa);
        transferencia.setDataTransferencia(dto.getDataTransferencia());
        transferencia.setDataAgendamento(dataAgendamento);

        Transferencia taxaSalva = transferenciaRepository.save(transferencia);

        return convertToResponseDTO(taxaSalva);
    }

    @Transactional(readOnly = true)
    public Page<TransferenciaResponseDTO> listarExtrato(Pageable pageable) {
        Page<Transferencia> transferencias = transferenciaRepository.findAllByOrderByDataAgendamentoDesc(pageable);
        return transferencias.map(this::convertToResponseDTO);

    }

    private TransferenciaResponseDTO convertToResponseDTO(Transferencia transferencia) {
        return TransferenciaResponseDTO.builder()
                .id(transferencia.getId())
                .contaOrigem(transferencia.getContaOrigem())
                .contaDestino(transferencia.getContaDestino())
                .valorTransferencia(transferencia.getValorTransferencia())
                .taxa(transferencia.getTaxa())
                .dataTransferencia(transferencia.getDataTransferencia())
                .dataAgendamento(transferencia.getDataAgendamento())
                .build();
    }
}
