package desafio.vr.miniautorizador.services;

import desafio.vr.miniautorizador.dtos.CartaoDto;
import desafio.vr.miniautorizador.models.Cartao;
import desafio.vr.miniautorizador.repositories.CartaoRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CartaoService.class)
public class CartaoServiceTest {

    @Autowired
    private CartaoService subject;

    @MockBean
    private CartaoRepository cartaoRepository;

    @Test
    public void deveRetornar422SeCartaoJaExiste() {
        CartaoDto dto = Mockito.mock(CartaoDto.class);
        Mockito.when(cartaoRepository.existsById(dto.getNumeroCartao())).thenReturn(true);
        ResponseStatusException ex = Assert.assertThrows(ResponseStatusException.class, () -> subject.criarCartao(dto));
        verify(cartaoRepository, times(1)).existsById(dto.getNumeroCartao());
        Assert.assertTrue(ex.getStatus().equals(HttpStatus.UNPROCESSABLE_ENTITY));
    }

    @Test
    public void deveInvocarCreateNoBDSeCartaoNaoExiste() {
        CartaoDto dto = Mockito.mock(CartaoDto.class);
        Cartao cartao = Mockito.mock(Cartao.class);
        Mockito.when(cartaoRepository.existsById(dto.getNumeroCartao())).thenReturn(false);
        Mockito.when(cartaoRepository.save(any(Cartao.class))).thenReturn(cartao);
        subject.criarCartao(dto);
        verify(cartaoRepository, times(1)).existsById(dto.getNumeroCartao());
        verify(cartaoRepository, times(1)).save(any(Cartao.class));
    }

    @Test
    public void deveRetornar404SeSaldoConsultadoDeCartaoInexistente() {
        String numeroCartao = Mockito.anyString();
        Mockito.when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.empty());
        ResponseStatusException ex = Assert.assertThrows(ResponseStatusException.class, () -> subject.consultarSaldo(numeroCartao));
        Assert.assertTrue(ex.getStatus().equals(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void deveRetornarSaldoSeConsultarEstiverOk() {
        String numeroCartao = "numerocartao";
        Cartao cartao = Mockito.mock(Cartao.class);
        Mockito.when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.of(cartao));
        Mockito.when(cartao.getSaldo()).thenReturn(500.27);
        Assert.assertEquals(subject.consultarSaldo(numeroCartao), String.valueOf(500.27));
    }
}
