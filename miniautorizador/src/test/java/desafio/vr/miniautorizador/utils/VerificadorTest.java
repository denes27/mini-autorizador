package desafio.vr.miniautorizador.utils;

import desafio.vr.miniautorizador.dtos.CartaoDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Verificador.class)
public class VerificadorTest {

    @Autowired
    private Verificador subject;

    @Test
    public void deveRetornarTrueAoVerificarCartaoDtoCorreto() {
        Assert.assertTrue(subject.verificarCartaoDto(new CartaoDto("1234567891234567", "senha")));
    }

    @Test
    public void deveRetornarFalseAoVerificarCartaoDtoIncorreto() {
        Assert.assertTrue(!subject.verificarCartaoDto(new CartaoDto("12345678", "")));
    }
}
