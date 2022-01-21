package desafio.vr.miniautorizador.services;

import desafio.vr.miniautorizador.dtos.CartaoDto;
import desafio.vr.miniautorizador.models.Cartao;
import desafio.vr.miniautorizador.repositories.CartaoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository repository;

    private Logger logger = LogManager.getLogger("CartaoService");

    public CartaoDto criarCartao(CartaoDto novoCartaoDto) {
        if(repository.existsById(novoCartaoDto.getNumeroCartao())) {
            logger.error("Tentativa de criar cartão já existente. Número: " + novoCartaoDto.getNumeroCartao());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cartão já existe");
        }
        Cartao novoCartao = repository.save(new Cartao(novoCartaoDto.getNumeroCartao(), 500.00, novoCartaoDto.getSenha()));
        logger.info("Novo cartão criado. Número: " + novoCartaoDto.getNumeroCartao());
        return new CartaoDto(novoCartao.getId(), novoCartao.getSenha());
    }

    public Iterable<Cartao> listarCartoes () {
        return repository.findAll();
    }
}
