package br.com.agendamentotransferencias.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transferencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Conta de origem é obrigatória")
    @Pattern(regexp = "\\d{10}", message = "Conta de origem deve ter 10 dígitos")
    @Column(nullable = false, length = 10)
    private String contaOrigem;

    @NotBlank(message = "Conta de destino é obrigatória")
    @Pattern(regexp = "\\d{10}", message = "Conta de destino deve ter 10 dígitos")
    @Column(nullable = false, length = 10)
    private String contaDestino;

    @NotNull(message = "Valor da transferência é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTransferencia;

    @NotNull(message = "Taxa é obrigatória")
    @DecimalMin(value = "0.00", message = "Taxa não pode ser negativa")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal taxa;

    @NotNull(message = "Data da transferência é obrigatória")
    @Column(nullable = false)
    private LocalDate dataTransferencia;

    @NotNull(message = "Data de agendamento é obrigatória")
    @Column(nullable = false)
    private LocalDate dataAgendamento;

    @PrePersist
    protected void onCreate() {
        if (dataAgendamento == null) {
            dataAgendamento = LocalDate.now();
        }
    }
}
