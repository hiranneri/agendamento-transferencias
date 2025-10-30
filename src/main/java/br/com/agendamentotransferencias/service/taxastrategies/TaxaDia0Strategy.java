package br.com.agendamentotransferencias.service.taxastrategies;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class TaxaDia0Strategy implements TaxaStrategy {

    @Override
    public boolean aplica(long dias) {
        return dias <= 0;
    }

    @Override
    public BigDecimal calcular(BigDecimal valorTransferencia) {
        BigDecimal valorFixo = new BigDecimal("3.00");
        BigDecimal percentual = new BigDecimal("0.025");
        return valorFixo.add(valorTransferencia.multiply(percentual))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
