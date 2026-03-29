package Projects.ExpressionTree;

public class WatsonContext {
    private String formulaAtual;
    private boolean ultimosResultados[];
    private int ultimaLinhaExplicada = -1;
    private String modo = "PROFESSOR";

    public WatsonContext(String formulaAtual, boolean[] ultimosResultados, int ultimaLinhaExplicada, String modo) {
        this.formulaAtual = formulaAtual;
        this.ultimosResultados = ultimosResultados;
        this.ultimaLinhaExplicada = ultimaLinhaExplicada;
        this.modo = modo;
    }

    public String getFormulaAtual() {
        return formulaAtual;
    }

    public void setFormulaAtual(String formulaAtual) {
        this.formulaAtual = formulaAtual;
    }

    public boolean[] getUltimosResultados() {
        return ultimosResultados;
    }

    public void setUltimosResultados(boolean[] ultimosResultados) {
        this.ultimosResultados = ultimosResultados;
    }

    public int getUltimaLinhaExplicada() {
        return ultimaLinhaExplicada;
    }

    public void setUltimaLinhaExplicada(int ultimaLinhaExplicada) {
        this.ultimaLinhaExplicada = ultimaLinhaExplicada;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }
}
