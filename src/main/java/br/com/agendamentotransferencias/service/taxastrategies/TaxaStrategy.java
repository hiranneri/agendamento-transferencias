package br.com.agendamentotransferencias.service.taxastrategies;

import java.math.BigDecimal;

public interface TaxaStrategy {

    boolean aplica(long dias);

    BigDecimal calcular(BigDecimal valorTransferencia);
}
