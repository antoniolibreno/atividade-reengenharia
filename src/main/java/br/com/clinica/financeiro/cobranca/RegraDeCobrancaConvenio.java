package br.com.clinica.financeiro.cobranca;

public class RegraDeCobrancaConvenio implements RegraDeCobranca {
    private static final double PERCENTUAL_DESCONTO = 0.12;
    private static final double TAXA_ADICIONAL = 15.50;

    @Override
    public double aplicar(double subtotal, boolean cobrarTaxaAdicional) {
        double valorFinal = subtotal - (subtotal * PERCENTUAL_DESCONTO);
        if (cobrarTaxaAdicional) {
            valorFinal += TAXA_ADICIONAL;
        }
        return valorFinal;
    }
}
