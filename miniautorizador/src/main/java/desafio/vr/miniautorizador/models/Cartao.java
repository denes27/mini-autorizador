package desafio.vr.miniautorizador.models;

import org.springframework.data.annotation.Id;

public class Cartao {

    @Id
    private String numeroCartao;
    private double saldo;
    private String senha;

    public Cartao(String numeroCartao, double saldo, String senha) {
        this.numeroCartao = numeroCartao;
        this.saldo = saldo;
        this.senha = senha;
    }

    public String getId() {
        return numeroCartao;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getSenha() {
        return senha;
    }

}
