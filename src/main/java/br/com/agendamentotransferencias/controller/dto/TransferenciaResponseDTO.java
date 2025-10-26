package br.com.agendamentotransferencias.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferenciaResponseDTO {

    private Long id;
    private String contaOrigem;
    private String contaDestino;
    private BigDecimal valorTransferencia;
    private BigDecimal taxa;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataTransferencia;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAgendamento;
}
