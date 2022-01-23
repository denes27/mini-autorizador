package desafio.vr.miniautorizador.controllers;

import desafio.vr.miniautorizador.dtos.CartaoDto;
import desafio.vr.miniautorizador.dtos.TransacaoDto;
import desafio.vr.miniautorizador.exceptions.CartaoInvalidoException;
import desafio.vr.miniautorizador.exceptions.SaldoInsuficienteException;
import desafio.vr.miniautorizador.exceptions.SenhaInvalidaException;
import desafio.vr.miniautorizador.models.Cartao;
import desafio.vr.miniautorizador.services.CartaoService;
import desafio.vr.miniautorizador.utils.Verificador;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;

@Controller
public class CartaoController {

    @Autowired
    private Verificador verificador;

    @Autowired
    private CartaoService service;

    private Logger logger = LogManager.getLogger("CartaoController");

    @GetMapping("/cartoes")
    private ResponseEntity<Iterable<Cartao>> listarCartoes() {
        return new ResponseEntity<Iterable<Cartao>>(service.listarCartoes(), HttpStatus.OK);
    }

    @PostMapping("/cartoes")
    private ResponseEntity<CartaoDto> criarCartao(@RequestBody(required = true) @Valid CartaoDto novoCartao) {
        logger.info("Criação de cartão requisitada. Número: " + novoCartao.getNumeroCartao());

        try {
            return new ResponseEntity<CartaoDto>(service.criarCartao(novoCartao), HttpStatus.CREATED);
        } catch (ResponseStatusException ex) {
            return new ResponseEntity<CartaoDto>(novoCartao, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/cartoes/{numeroCartao}")
    private ResponseEntity<String> consultarSaldo(@PathVariable("numeroCartao") String numeroCartao) {
        try {
            return new ResponseEntity<String>(service.consultarSaldo(numeroCartao), HttpStatus.OK);
        } catch (ResponseStatusException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/transacoes")
    private ResponseEntity<String> efetuarTransacao(@RequestBody @Valid TransacaoDto transacaoDto) {
        try {
            return new ResponseEntity<String>(service.realizarTransacao(transacaoDto), HttpStatus.CREATED);
        } catch (CartaoInvalidoException ex) {
            return new ResponseEntity<String>("CARTAO_INEXISTENTE", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (SaldoInsuficienteException ex) {
            return new ResponseEntity<String>("SALDO_INSUFICIENTE", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (SenhaInvalidaException ex) {
            return new ResponseEntity<String>("SENHA_INVALIDA", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
