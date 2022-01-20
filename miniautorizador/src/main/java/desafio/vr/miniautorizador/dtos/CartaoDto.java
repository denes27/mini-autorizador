package desafio.vr.miniautorizador.dtos;

public class CartaoDto {
    private String numeroCartao;
    private String senha;

    public CartaoDto() {

    }

    public CartaoDto(String numeroCartao, String senha) {
        this.numeroCartao = numeroCartao;
        this.senha = senha;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public String getSenha() {
        return senha;
    }
}
