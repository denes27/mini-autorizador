package desafio.vr.miniautorizador.models;

import org.springframework.data.annotation.Id;

public class Cartao {

    @Id
    private int id;
    private float saldo;
    private String senha;

    public Cartao(int id, float saldo, String senha) {
        this.id = id;
        this.saldo = saldo;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public String getSenha() {
        return senha;
    }
}
