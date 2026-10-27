package br.com.clinica.financeiro;

import br.com.clinica.financeiro.atendimento.CalculadoraDeAtendimento;
import br.com.clinica.financeiro.atendimento.SomadorDeProcedimentos;
import br.com.clinica.financeiro.cobranca.TipoConsulta;
import br.com.clinica.financeiro.legado.x_Mgr_F;

/**
 * Executa cada cenário no código novo e no legado e compara os resultados.
 * As linhas "LOG:" são impressas pela classe legada, que ainda contém o println.
 */
public class Main {

    private static final CalculadoraDeAtendimento calculadora =
            new CalculadoraDeAtendimento(new SomadorDeProcedimentos());
    private static final x_Mgr_F legado = new x_Mgr_F();

    public static void main(String[] args) {
        verificar("Convênio com taxa (cenário da atividade)", 1, new double[]{200.0, 100.0}, true, 279.50);
        verificar("Convênio sem taxa", 1, new double[]{200.0, 100.0}, false, 264.00);
        verificar("Particular acima de 500", 2, new double[]{300.0, 300.0}, false, 570.00);
        verificar("Particular com 500 exatos", 2, new double[]{250.0, 250.0}, false, 500.00);
        verificar("Tipo não identificado", 3, new double[]{200.0, 100.0}, false, 300.00);
        verificar("Convênio sem procedimentos", 1, new double[]{}, true, 15.50);
    }

    private static void verificar(String cenario, int codigoTipo, double[] procedimentos,
                                  boolean cobrarTaxaAdicional, double valorEsperado) {
        double valorNovo = calculadora.calcularValorFinal(
                TipoConsulta.doCodigo(codigoTipo), procedimentos, cobrarTaxaAdicional);
        double valorLegado = legado.calc(codigoTipo, procedimentos, cobrarTaxaAdicional);

        boolean igualAoLegado = valorNovo == valorLegado;
        boolean igualAoEsperado = Math.abs(valorNovo - valorEsperado) < 0.001;
        String status = (igualAoLegado && igualAoEsperado) ? "OK" : "FALHOU";

        System.out.printf("[%s] %s | novo: %.2f | legado: %.2f | esperado: %.2f%n",
                status, cenario, valorNovo, valorLegado, valorEsperado);
    }
}
