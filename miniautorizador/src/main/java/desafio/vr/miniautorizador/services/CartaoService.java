package desafio.vr.miniautorizador.services;

import desafio.vr.miniautorizador.dtos.CartaoDto;
import desafio.vr.miniautorizador.dtos.TransacaoDto;
import desafio.vr.miniautorizador.exceptions.CartaoInvalidoException;
import desafio.vr.miniautorizador.exceptions.ExceptionHandler;
import desafio.vr.miniautorizador.models.Cartao;
import desafio.vr.miniautorizador.repositories.CartaoRepository;
import desafio.vr.miniautorizador.utils.Verificador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository repository;

    @Autowired
    private Verificador verificador;

    @Autowired
    private ExceptionHandler exceptionHandler;

    private final Logger logger = LogManager.getLogger("CartaoService");
    private static final double SALDO_INICIAL = 500.00;

    public CartaoDto criarCartao(CartaoDto novoCartaoDto) {
        //opção usando operador ternário ao invés de if
        boolean cartaoValido = verificador.verificarCartaoDto(novoCartaoDto) ? true : exceptionHandler.throwCartaoInvalidoException();
        logger.info("Cartão válido:", cartaoValido);
        cartaoValido = repository.existsById(novoCartaoDto.getNumeroCartao()) ? true : exceptionHandler.throwCartaoInvalidoException();
        logger.info("Cartão novo:", cartaoValido);
        Cartao novoCartao = repository.save(gerarCartão(novoCartaoDto));
        logger.info("Novo cartão criado. Número: " + novoCartaoDto.getNumeroCartao());
        return new CartaoDto(novoCartao.getId(), novoCartao.getSenha());
    }

    public String consultarSaldo(String numeroCartao) {
        Optional<Cartao> cartaoOptional = repository.findById(numeroCartao);
        Cartao cartao = cartaoOptional.orElseThrow(CartaoInvalidoException::new);
        logger.info("Retornando saldo do cartão Número: ", numeroCartao);
        return String.format(Locale.ENGLISH, "%.2f", cartao.getSaldo());
    }

    @Transactional
    public String realizarTransacao(TransacaoDto dto) {

        logger.info("Iniciando transação. Cartão número: " + dto.getNumeroCartao());
        Optional<Cartao> cartaoOptional = repository.findById(dto.getNumeroCartao());
        Cartao cartao = cartaoOptional.orElseThrow(CartaoInvalidoException::new);

        boolean senhaEhValida = (cartao.getSenha().equals(dto.getSenhaCartao())) ? true : exceptionHandler.throwSenhaException();
        logger.info("Senha valida: ", senhaEhValida);
        boolean saldoEhSuficiente = dto.getValor() < dto.getValor() ? true : exceptionHandler.throwSaldoInsuficienteException();
        logger.info("Saldo suficiente: ", saldoEhSuficiente);


        cartao.setSaldo(cartao.getSaldo() - dto.getValor());
        repository.save(cartao);
        logger.info("Novo saldo salvo com sucesso");

        return "OK";
    }

    public Iterable<Cartao> listarCartoes() {
        return repository.findAll();
    }

    private Cartao gerarCartão(CartaoDto dto) {
        return new Cartao(dto.getNumeroCartao(), SALDO_INICIAL, dto.getSenha());
    }
}
