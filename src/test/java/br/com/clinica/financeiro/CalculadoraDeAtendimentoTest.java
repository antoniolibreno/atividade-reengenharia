package br.com.clinica.financeiro;

import br.com.clinica.financeiro.atendimento.CalculadoraDeAtendimento;
import br.com.clinica.financeiro.atendimento.SomadorDeProcedimentos;
import br.com.clinica.financeiro.cobranca.TipoConsulta;
import br.com.clinica.financeiro.legado.x_Mgr_F;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Teste de caracterização: o código novo deve devolver exatamente o mesmo valor que o legado.
 */
class CalculadoraDeAtendimentoTest {

    private static final double TOLERANCIA = 0.001;

    private final CalculadoraDeAtendimento calculadora =
            new CalculadoraDeAtendimento(new SomadorDeProcedimentos());
    private final x_Mgr_F legado = new x_Mgr_F();

    private void verificarEquivalencia(int codigoTipo, double[] procedimentos,
                                       boolean cobrarTaxaAdicional, double valorEsperado) {
        double valorNovo = calculadora.calcularValorFinal(
                TipoConsulta.doCodigo(codigoTipo), procedimentos, cobrarTaxaAdicional);
        double valorLegado = legado.calc(codigoTipo, procedimentos, cobrarTaxaAdicional);

        assertEquals(valorLegado, valorNovo, "O código novo deve ser idêntico ao legado");
        assertEquals(valorEsperado, valorNovo, TOLERANCIA, "O valor deve bater com o cálculo manual");
    }

    @Test
    @DisplayName("Convênio com taxa: cenário exigido pela atividade (279,50)")
    void convenioComTaxa() {
        verificarEquivalencia(1, new double[]{200.0, 100.0}, true, 279.50);
    }

    @Test
    @DisplayName("Convênio sem taxa: apenas desconto de 12%")
    void convenioSemTaxa() {
        verificarEquivalencia(1, new double[]{200.0, 100.0}, false, 264.00);
    }

    @Test
    @DisplayName("Particular acima de 500: desconto de 5%")
    void particularAcimaDoLimite() {
        verificarEquivalencia(2, new double[]{300.0, 300.0}, false, 570.00);
    }

    @Test
    @DisplayName("Particular com 500 exatos: sem desconto")
    void particularNoLimite() {
        verificarEquivalencia(2, new double[]{250.0, 250.0}, false, 500.00);
    }

    @Test
    @DisplayName("Tipo não identificado: nenhuma regra aplicada")
    void tipoNaoIdentificado() {
        verificarEquivalencia(3, new double[]{200.0, 100.0}, false, 300.00);
    }

    @Test
    @DisplayName("Convênio sem procedimentos: cobra somente a taxa (comportamento herdado do legado)")
    void convenioSemProcedimentos() {
        verificarEquivalencia(1, new double[]{}, true, 15.50);
    }
}
