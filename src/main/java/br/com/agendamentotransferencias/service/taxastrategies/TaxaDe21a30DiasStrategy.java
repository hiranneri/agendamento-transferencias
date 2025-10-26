package br.com.agendamentotransferencias.service.taxastrategies;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TaxaDe21a30DiasStrategy implements TaxaStrategy {

    @Override
    public boolean aplica(long dias) {
        return dias >= 21 && dias <= 30;
    }

    @Override
    public BigDecimal calcular(BigDecimal valorTransferencia) {
        return valorTransferencia.multiply(new BigDecimal("0.069"));
    }
}
