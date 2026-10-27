package br.com.clinica.financeiro.atendimento;

public class SomadorDeProcedimentos {

    public double somar(double[] procedimentosRealizados) {
        double subtotal = 0;
        for (double valorProcedimento : procedimentosRealizados) {
            subtotal += valorProcedimento;
        }
        return subtotal;
    }
}
