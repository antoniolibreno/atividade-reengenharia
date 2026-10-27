package br.com.clinica.financeiro.cobranca;

public enum TipoConsulta {
    CONVENIO,
    PARTICULAR,
    OUTRO;

    public static TipoConsulta doCodigo(int codigo) {
        return switch (codigo) {
            case 1 -> CONVENIO;
            case 2 -> PARTICULAR;
            default -> OUTRO;
        };
    }
}
