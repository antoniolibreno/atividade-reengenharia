package br.com.clinica.financeiro.atendimento;

import br.com.clinica.financeiro.cobranca.FabricaDeRegrasDeCobranca;
import br.com.clinica.financeiro.cobranca.RegraDeCobranca;
import br.com.clinica.financeiro.cobranca.TipoConsulta;

public class CalculadoraDeAtendimento {

    private final SomadorDeProcedimentos somadorDeProcedimentos;

    public CalculadoraDeAtendimento(SomadorDeProcedimentos somadorDeProcedimentos) {
        this.somadorDeProcedimentos = somadorDeProcedimentos;
    }

    public double calcularValorFinal(TipoConsulta tipoConsulta,
                                     double[] procedimentosRealizados,
                                     boolean cobrarTaxaAdicional) {
        double subtotal = somadorDeProcedimentos.somar(procedimentosRealizados);
        RegraDeCobranca regraDeCobranca = FabricaDeRegrasDeCobranca.criarRegraDeCobranca(tipoConsulta);
        return regraDeCobranca.aplicar(subtotal, cobrarTaxaAdicional);
    }
}
