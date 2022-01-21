package desafio.vr.miniautorizador.dtos;

import javax.validation.constraints.NotBlank;

public class TransacaoDto {

    @NotBlank
    private String numeroCartao;
    @NotBlank
    private String senhaCartao;
    @NotBlank
    private double valor;

    public TransacaoDto(String numeroCartao, String senha, double valor) {
        this.numeroCartao = numeroCartao;
        this.senhaCartao = senha;
        this.valor = valor;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public String getSenhaCartao() {
        return senhaCartao;
    }

    public double getValor() {
        return valor;
    }
}
