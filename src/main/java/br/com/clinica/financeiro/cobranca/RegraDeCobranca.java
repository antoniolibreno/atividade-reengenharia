package br.com.clinica.financeiro.cobranca;

public interface RegraDeCobranca {
    double aplicar(double subtotal, boolean cobrarTaxaAdicional);
}
