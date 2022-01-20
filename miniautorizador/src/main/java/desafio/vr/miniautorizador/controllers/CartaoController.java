package desafio.vr.miniautorizador.controllers;

import desafio.vr.miniautorizador.dtos.CartaoDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CartaoController {

    @PostMapping("/cartoes")
    private ResponseEntity<CartaoDto> criarCartao(@RequestBody(required = true) CartaoDto novoCartao) {
        return new ResponseEntity<CartaoDto>(novoCartao, HttpStatus.CREATED);
    }
}
