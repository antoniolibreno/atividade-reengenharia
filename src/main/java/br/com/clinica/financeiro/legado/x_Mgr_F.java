package br.com.clinica.financeiro.legado;

/**
 * Código legado original, mantido sem nenhuma alteração.
 * Serve apenas como referência para provar que a reengenharia preservou o comportamento.
 */
public class x_Mgr_F {
    public double calc(int tp, double[] vls, boolean st) {
        double r = 0;
        for (int i = 0; i < vls.length; i++) {
            r += vls[i];
        }
        if (tp == 1) {
            r = r - (r * 0.12);
            if (st) {
                r += 15.50;
            }
        } else if (tp == 2) {
            if (r > 500) {
                r = r - (r * 0.05);
            }
        }
        System.out.println("LOG: " + r);
        return r;
    }
}
