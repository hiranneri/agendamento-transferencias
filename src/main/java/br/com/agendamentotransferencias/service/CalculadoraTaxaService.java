package br.com.agendamentotransferencias.service;

import br.com.agendamentotransferencias.controller.exceptions.TaxaNaoAplicavelException;
import br.com.agendamentotransferencias.service.taxastrategies.TaxaStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CalculadoraTaxaService {

    @Autowired
    private List<TaxaStrategy> estrategias;

    public BigDecimal calcular(CalculoTaxaDTO calculoTaxaDTO) {
        long diasAteTransferencia = ChronoUnit.DAYS.between(calculoTaxaDTO.getDataTransferencia(), calculoTaxaDTO.getDataAgendamento());

        if (diasAteTransferencia < 0) {
            throw new TaxaNaoAplicavelException("Data da transferência não pode ser anterior à data de agendamento");
        }

        return estrategias.stream()
                .filter(e -> e.aplica(diasAteTransferencia))
                .findFirst()
                .map(e -> e.calcular(calculoTaxaDTO.getValorTransferencia()).setScale(2, RoundingMode.HALF_UP))
                .orElseThrow(() -> new TaxaNaoAplicavelException(
                        String.format("Não há taxa aplicável para transferências agendadas com mais de 50 dias de antecedência. Dias calculados: %d", diasAteTransferencia)
                ));

    }
}
