package br.com.agendamentotransferencias.service.taxastrategies;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class TaxaDe41a50DiasStrategy implements TaxaStrategy {

    @Override
    public boolean aplica(long dias) {
        return dias >= 41 && dias <= 50;
    }

    @Override
    public BigDecimal calcular(BigDecimal valorTransferencia) {
        BigDecimal percentual = new BigDecimal("0.017");
        return valorTransferencia.multiply(percentual)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
