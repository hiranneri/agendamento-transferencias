package br.com.agendamentotransferencias.service.taxastrategies;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TaxaDe11a20DiasStrategy implements TaxaStrategy {

    @Override
    public boolean aplica(long dias) {
        return dias >= 11 && dias <= 20;
    }

    @Override
    public BigDecimal calcular(BigDecimal valorTransferencia) {
        return valorTransferencia.multiply(new BigDecimal("0.082"));
    }
}
