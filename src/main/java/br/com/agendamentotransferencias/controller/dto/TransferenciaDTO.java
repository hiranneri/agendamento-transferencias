package br.com.agendamentotransferencias.controller.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaDTO {

    @NotBlank(message = "Conta de origem é obrigatória")
    @Pattern(regexp = "\\d{10}", message = "Conta de origem deve ter exatamente 10 dígitos")
    private String contaOrigem;

    @NotBlank(message = "Conta de destino é obrigatória")
    @Pattern(regexp = "\\d{10}", message = "Conta de destino deve ter exatamente 10 dígitos")
    private String contaDestino;

    @NotNull(message = "Valor da transferência é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valorTransferencia;

    @NotNull(message = "Data da transferência é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataTransferencia;
}
