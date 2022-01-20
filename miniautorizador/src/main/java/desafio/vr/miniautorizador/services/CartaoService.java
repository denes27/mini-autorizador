package desafio.vr.miniautorizador.services;

import desafio.vr.miniautorizador.dtos.CartaoDto;
import desafio.vr.miniautorizador.models.Cartao;
import desafio.vr.miniautorizador.repositories.CartaoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartaoService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CartaoRepository repository;

    public CartaoDto criarCartao(CartaoDto novoCartaoDto) {
        Cartao novoCartao = repository.save(new Cartao(novoCartaoDto.getNumeroCartao(), 500.00, novoCartaoDto.getSenha()));
        return new CartaoDto(novoCartao.getId(), novoCartao.getSenha());
    }

    public Iterable<Cartao> listarCartoes () {
        return repository.findAll();
    }
}
