package br.com.clinica.financeiro.cobranca;

public final class FabricaDeRegrasDeCobranca {

    private FabricaDeRegrasDeCobranca() {
    }

    public static RegraDeCobranca criarRegraDeCobranca(TipoConsulta tipoConsulta) {
        return switch (tipoConsulta) {
            case CONVENIO -> new RegraDeCobrancaConvenio();
            case PARTICULAR -> new RegraDeCobrancaParticular();
            case OUTRO -> new RegraDeCobrancaOutro();
        };
    }
}
