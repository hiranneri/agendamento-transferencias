package br.com.agendamentotransferencias.service.taxastrategies;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class TaxaDe31a40DiasStrategy implements TaxaStrategy {

    @Override
    public boolean aplica(long dias) {
        return dias >= 31 && dias <= 40;
    }

    @Override
    public BigDecimal calcular(BigDecimal valorTransferencia) {
        BigDecimal percentual = new BigDecimal("0.047");
        return valorTransferencia.multiply(percentual)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
