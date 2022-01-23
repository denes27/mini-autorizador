package desafio.vr.miniautorizador.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotBlank;

public class CartaoDto {

    @NotBlank
    private String numeroCartao;
    @NotBlank
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
