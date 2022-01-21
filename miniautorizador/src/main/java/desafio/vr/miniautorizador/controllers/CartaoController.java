package desafio.vr.miniautorizador.controllers;

import desafio.vr.miniautorizador.dtos.CartaoDto;
import desafio.vr.miniautorizador.models.Cartao;
import desafio.vr.miniautorizador.services.CartaoService;
import desafio.vr.miniautorizador.utils.Verificador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class CartaoController {

    @Autowired
    private Verificador verificador;

    @Autowired
    private CartaoService service;

    @GetMapping("/cartoes")
    private ResponseEntity<Iterable<Cartao>> listarCartoes() {
        return new ResponseEntity<Iterable<Cartao>>(service.listarCartoes(), HttpStatus.OK);
    }

    @PostMapping("/cartoes")
    private ResponseEntity<CartaoDto> criarCartao(@RequestBody(required = true) CartaoDto novoCartao) {
        if(!verificador.verificarCartaoDto(novoCartao)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cartão inválido");
        }

        try {
            return new ResponseEntity<CartaoDto>(service.criarCartao(novoCartao), HttpStatus.CREATED);
        } catch (ResponseStatusException ex) {
            return new ResponseEntity<CartaoDto>(novoCartao, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
