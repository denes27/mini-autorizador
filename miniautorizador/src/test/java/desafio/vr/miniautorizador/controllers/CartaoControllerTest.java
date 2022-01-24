package desafio.vr.miniautorizador.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import desafio.vr.miniautorizador.dtos.CartaoDto;
import desafio.vr.miniautorizador.dtos.TransacaoDto;
import desafio.vr.miniautorizador.exceptions.CartaoInvalidoException;
import desafio.vr.miniautorizador.exceptions.SaldoInsuficienteException;
import desafio.vr.miniautorizador.exceptions.SenhaInvalidaException;
import desafio.vr.miniautorizador.services.CartaoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CartaoController.class)
@ContextConfiguration(classes = CartaoController.class)
public class CartaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartaoService cartaoService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void deveRetornarBadRequestSeBuscarSaldoDeCartaoInvalido() throws Exception {
        String uri = "/cartoes/1234";
        when(cartaoService.consultarSaldo(anyString())).thenThrow(CartaoInvalidoException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.ALL))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void deveRetornar200SeSaldoConsultadoComSucesso() throws Exception {
        String uri = "/cartoes/1234567891234567";
        when(cartaoService.consultarSaldo(anyString())).thenReturn("500.00");
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    public void deveRetornar201SeCartaoCriadoComSucesso() throws Exception {
        String uri = "/cartoes";
        CartaoDto dto = new CartaoDto("numero", "senha");
        when(cartaoService.criarCartao(any(CartaoDto.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void deveRetornar422SeTentativaDeCriarCartaoInvalido() throws Exception {
        String uri = "/cartoes";
        CartaoDto dto = new CartaoDto("numero", "senha");
        when(cartaoService.criarCartao(any(CartaoDto.class))).thenThrow(CartaoInvalidoException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void deveRetornar201SeTransacaoEfetuadaComSucesso() throws Exception {
        String uri = "/transacoes";
        TransacaoDto dto = new TransacaoDto("numero", "senha", 10.00);
        when(cartaoService.realizarTransacao(any(TransacaoDto.class))).thenReturn("OK");

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void deveRetornar422SeTransacaoComCartaoInexistente() throws Exception {
        String uri = "/transacoes";
        TransacaoDto dto = new TransacaoDto("numero", "senha", 10.00);
        when(cartaoService.realizarTransacao(any(TransacaoDto.class))).thenThrow(CartaoInvalidoException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void deveRetornar422SeTransacaoComSaldoInsuficiente() throws Exception {
        String uri = "/transacoes";
        TransacaoDto dto = new TransacaoDto("numero", "senha", 10.00);
        when(cartaoService.realizarTransacao(any(TransacaoDto.class))).thenThrow(SaldoInsuficienteException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void deveRetornar422SeTransacaoComSenhaInvalida() throws Exception {
        String uri = "/transacoes";
        TransacaoDto dto = new TransacaoDto("numero", "senha", 10.00);
        when(cartaoService.realizarTransacao(any(TransacaoDto.class))).thenThrow(SenhaInvalidaException .class);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }
}
