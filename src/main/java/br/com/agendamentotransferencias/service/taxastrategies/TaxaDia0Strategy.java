package br.com.agendamentotransferencias.service.taxastrategies;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TaxaDia0Strategy implements TaxaStrategy {

    @Override
    public boolean aplica(long dias) {
        return dias <= 0;
    }

    @Override
    public BigDecimal calcular(BigDecimal valorTransferencia) {
        BigDecimal taxaFixa = new BigDecimal("3.00");
        BigDecimal taxaPercentual = valorTransferencia.multiply(new BigDecimal("0.025"));
        return taxaFixa.add(taxaPercentual);
    }
}
