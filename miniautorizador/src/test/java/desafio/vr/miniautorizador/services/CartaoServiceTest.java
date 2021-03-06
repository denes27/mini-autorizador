package desafio.vr.miniautorizador.services;

import desafio.vr.miniautorizador.dtos.CartaoDto;
import desafio.vr.miniautorizador.dtos.TransacaoDto;
import desafio.vr.miniautorizador.exceptions.CartaoInvalidoException;
import desafio.vr.miniautorizador.exceptions.ExceptionHandler;
import desafio.vr.miniautorizador.exceptions.SaldoInsuficienteException;
import desafio.vr.miniautorizador.exceptions.SenhaInvalidaException;
import desafio.vr.miniautorizador.models.Cartao;
import desafio.vr.miniautorizador.repositories.CartaoRepository;
import desafio.vr.miniautorizador.utils.Verificador;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CartaoService.class)
public class CartaoServiceTest {

    @Autowired
    private CartaoService subject;

    @MockBean
    private CartaoRepository cartaoRepository;

    @MockBean
    private Verificador verificador;

    @MockBean
    private ExceptionHandler exceptionHandler;

    @Test
    public void deveLanÃ§arExcecaoSeTentativaDeCriarCartaoJaExistente() {
        CartaoDto dto = Mockito.mock(CartaoDto.class);
        Mockito.when(verificador.verificarCartaoDto(dto)).thenReturn(true);
        Mockito.when(cartaoRepository.existsById(dto.getNumeroCartao())).thenReturn(true);
        Mockito.when(exceptionHandler.throwCartaoInvalidoException()).thenThrow(CartaoInvalidoException.class);
        CartaoInvalidoException ex = Assert.assertThrows(CartaoInvalidoException.class, () -> subject.criarCartao(dto));
        verify(cartaoRepository, times(1)).existsById(dto.getNumeroCartao());
    }

    @Test
    public void deveInvocarCreateNoBDSeCartaoNaoExiste() {
        CartaoDto dto = Mockito.mock(CartaoDto.class);
        Cartao cartao = Mockito.mock(Cartao.class);
        Mockito.when(cartaoRepository.existsById(dto.getNumeroCartao())).thenReturn(true);
        Mockito.when(cartaoRepository.save(any(Cartao.class))).thenReturn(cartao);
        Mockito.when(verificador.verificarCartaoDto(dto)).thenReturn(true);
        subject.criarCartao(dto);
        verify(cartaoRepository, times(1)).existsById(dto.getNumeroCartao());
        verify(cartaoRepository, times(1)).save(any(Cartao.class));
    }

    @Test
    public void deveLancarExcecaoSeSaldoConsultadoDeCartaoInexistente() {
        String numeroCartao = Mockito.anyString();
        Mockito.when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.empty());
        Mockito.when(exceptionHandler.throwCartaoInvalidoException()).thenThrow(CartaoInvalidoException.class);
        CartaoInvalidoException ex = Assert.assertThrows(CartaoInvalidoException.class, () -> subject.consultarSaldo(numeroCartao));

    }

    @Test
    public void deveRetornarSaldoSeConsultarEstiverOk() {
        String numeroCartao = "numerocartao";
        Cartao cartao = Mockito.mock(Cartao.class);
        Mockito.when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.of(cartao));
        Mockito.when(cartao.getSaldo()).thenReturn(500.27);
        Assert.assertEquals(subject.consultarSaldo(numeroCartao), String.valueOf(500.27));
    }

    @Test
    public void deveRetornarExcecaodeSaldoInsuficienteSeSaldoNaoForSuficiente() {
        TransacaoDto dto = Mockito.mock(TransacaoDto.class);
        Cartao cartao = Mockito.mock(Cartao.class);
        Mockito.when(cartaoRepository.findById(dto.getNumeroCartao())).thenReturn(Optional.of(cartao));
        Mockito.when(exceptionHandler.throwSaldoInsuficienteException()).thenThrow(SaldoInsuficienteException.class);
        Mockito.when(cartao.getSaldo()).thenReturn(500.00);
        Mockito.when(cartao.getSenha()).thenReturn("senha");
        Mockito.when(dto.getValor()).thenReturn(1000.00);
        Mockito.when(dto.getSenhaCartao()).thenReturn("senha");
        SaldoInsuficienteException ex = Assert.assertThrows(SaldoInsuficienteException.class, () -> subject.realizarTransacao(dto));
    }

    @Test
    public void deveRetornarExcecaoDeCartaoInvalidoSeIdNaoExiste() {
        TransacaoDto dto = Mockito.mock(TransacaoDto.class);
        Cartao cartao = Mockito.mock(Cartao.class);
        Mockito.when(cartaoRepository.findById(dto.getNumeroCartao())).thenReturn(Optional.empty());
        CartaoInvalidoException ex = Assert.assertThrows(CartaoInvalidoException.class, () -> subject.realizarTransacao(dto));
    }

    @Test
    public void deveRetornarExcecaoDeSenhaInvalidaSeSenhaInvalida() {
        TransacaoDto dto = Mockito.mock(TransacaoDto.class);
        Cartao cartao = Mockito.mock(Cartao.class);
        Mockito.when(cartaoRepository.findById(dto.getNumeroCartao())).thenReturn(Optional.of(cartao));
        Mockito.when(dto.getSenhaCartao()).thenReturn("senha");
        Mockito.when(cartao.getSenha()).thenReturn("outrasenha");
        Mockito.when(exceptionHandler.throwSenhaException()).thenThrow(SenhaInvalidaException.class);
        SenhaInvalidaException exception = Assert.assertThrows(SenhaInvalidaException.class, () -> subject.realizarTransacao(dto));
    }

    @Test
    public void deveRetornarMensagemOkSeTransacaoForSucesso() {
        TransacaoDto dto = Mockito.mock(TransacaoDto.class);
        Cartao cartao = Mockito.mock(Cartao.class);
        Mockito.when(cartaoRepository.findById(dto.getNumeroCartao())).thenReturn(Optional.of(cartao));
        Mockito.when(cartao.getSaldo()).thenReturn(500.00);
        Mockito.when(cartao.getSenha()).thenReturn("senha");
        Mockito.when(dto.getValor()).thenReturn(500.00);
        Mockito.when(dto.getSenhaCartao()).thenReturn("senha");
        String result = subject.realizarTransacao(dto);
        verify(cartaoRepository, times(1)).save(cartao);
        Assert.assertEquals(result, "OK");
    }
}
