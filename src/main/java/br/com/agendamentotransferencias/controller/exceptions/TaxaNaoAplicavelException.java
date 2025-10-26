package br.com.agendamentotransferencias.controller.exceptions;

public class TaxaNaoAplicavelException extends RuntimeException {

    public TaxaNaoAplicavelException(String message) {
        super(message);
    }

    public TaxaNaoAplicavelException(String message, Throwable cause) {
        super(message, cause);
    }
}

