package br.com.agendamentotransferencias.service.taxastrategies;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TaxaDe31a40DiasStrategy implements TaxaStrategy {

    @Override
    public boolean aplica(long dias) {
        return dias >= 31 && dias <= 40;
    }

    @Override
    public BigDecimal calcular(BigDecimal valorTransferencia) {
        return valorTransferencia.multiply(new BigDecimal("0.047"));
    }
}
