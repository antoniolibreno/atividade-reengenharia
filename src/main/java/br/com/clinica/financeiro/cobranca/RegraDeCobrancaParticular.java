package br.com.clinica.financeiro.cobranca;

public class RegraDeCobrancaParticular implements RegraDeCobranca {
    // O desconto só vale para valores estritamente acima deste limite
    private static final double LIMITE_PARA_DESCONTO = 500.0;
    private static final double PERCENTUAL_DESCONTO = 0.05;

    @Override
    public double aplicar(double subtotal, boolean cobrarTaxaAdicional) {
        if (subtotal > LIMITE_PARA_DESCONTO) {
            return subtotal - (subtotal * PERCENTUAL_DESCONTO);
        }
        return subtotal;
    }
}
