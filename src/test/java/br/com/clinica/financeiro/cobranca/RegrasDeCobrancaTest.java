package br.com.clinica.financeiro.cobranca;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Testes unitários de cada regra isolada, possíveis graças à separação de responsabilidades.
 */
class RegrasDeCobrancaTest {

    private static final double TOLERANCIA = 0.001;

    @Test
    @DisplayName("Convênio aplica a taxa depois do desconto")
    void convenioAplicaTaxaDepoisDoDesconto() {
        RegraDeCobranca regra = new RegraDeCobrancaConvenio();
        assertEquals(279.50, regra.aplicar(300.0, true), TOLERANCIA);
    }

    @Test
    @DisplayName("Particular não dá desconto com 500 exatos")
    void particularSemDescontoNoLimite() {
        RegraDeCobranca regra = new RegraDeCobrancaParticular();
        assertEquals(500.0, regra.aplicar(500.0, false), TOLERANCIA);
    }

    @Test
    @DisplayName("Particular ignora a taxa adicional")
    void particularIgnoraTaxaAdicional() {
        RegraDeCobranca regra = new RegraDeCobrancaParticular();
        assertEquals(300.0, regra.aplicar(300.0, true), TOLERANCIA);
    }

    @Test
    @DisplayName("Fábrica devolve a regra correta para cada código")
    void fabricaDevolveRegraCorreta() {
        assertInstanceOf(RegraDeCobrancaConvenio.class,
                FabricaDeRegrasDeCobranca.criarRegraDeCobranca(TipoConsulta.doCodigo(1)));
        assertInstanceOf(RegraDeCobrancaParticular.class,
                FabricaDeRegrasDeCobranca.criarRegraDeCobranca(TipoConsulta.doCodigo(2)));
        assertInstanceOf(RegraDeCobrancaOutro.class,
                FabricaDeRegrasDeCobranca.criarRegraDeCobranca(TipoConsulta.doCodigo(99)));
    }
}
