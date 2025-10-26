package br.com.agendamentotransferencias.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoTaxaDTO {

    @NotNull(message = "Data de transferência é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataTransferencia;

    @NotNull(message = "Data de agendamento é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAgendamento;

    @NotNull(message = "Valor da transferência é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valorTransferencia;


}
