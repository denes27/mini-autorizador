package desafio.vr.miniautorizador.exceptions;

import org.springframework.stereotype.Component;

@Component
public class ExceptionHandler {

    public boolean throwSenhaException() {
        throw new SenhaInvalidaException();
    }

    public boolean throwSaldoInsuficienteException() {
        throw new SaldoInsuficienteException();
    }

    public boolean throwCartaoInvalidoException() {
        throw new CartaoInvalidoException();
    }

}
