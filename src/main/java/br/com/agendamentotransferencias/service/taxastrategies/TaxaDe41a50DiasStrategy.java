package br.com.agendamentotransferencias.service.taxastrategies;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TaxaDe41a50DiasStrategy implements TaxaStrategy {

    @Override
    public boolean aplica(long dias) {
        return dias >= 41 && dias <= 50;
    }

    @Override
    public BigDecimal calcular(BigDecimal valorTransferencia) {
        return valorTransferencia.multiply(new BigDecimal("0.017"));
    }
}
