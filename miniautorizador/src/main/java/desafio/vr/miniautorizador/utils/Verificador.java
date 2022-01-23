package desafio.vr.miniautorizador.utils;

import desafio.vr.miniautorizador.dtos.CartaoDto;
import org.springframework.stereotype.Component;

@Component
public class Verificador {
    //acrescentar qualquer regra de negocio relacionada a verificação de um cartão valido aqui
    public boolean verificarCartaoDto(CartaoDto cartao) {
        return !ehNuloOuVazio(cartao.getNumeroCartao()) && cartao.getNumeroCartao().length() == 16 && !ehNuloOuVazio(cartao.getSenha());
    }

    private boolean ehNuloOuVazio(String s) {
        return s == null || s.isBlank();
    }
}
