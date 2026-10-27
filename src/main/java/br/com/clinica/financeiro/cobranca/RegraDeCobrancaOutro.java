package br.com.clinica.financeiro.cobranca;

public class RegraDeCobrancaOutro implements RegraDeCobranca {
    @Override
    public double aplicar(double subtotal, boolean cobrarTaxaAdicional) {
        return subtotal;
    }
}
