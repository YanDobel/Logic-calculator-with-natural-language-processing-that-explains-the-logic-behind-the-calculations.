package Projects.ExpressionTree;
import java.util.*;
import java.util.regex.*;

public class WatsonEngine {
    private final Random random = new Random();
    private Modo modoAtual = Modo.PADRAO;

    public void setModoAtual(Modo novoModo) {
        this.modoAtual = novoModo;
    }

    public String explainLine(String op, boolean left, boolean right, boolean result) {
        String intro = generateIntro();
        String body = explainLogic(op, left, right, result);

        return intro + " " + body;
    }

    private String explainLogic(String op, boolean left, boolean right, boolean result) {
        String l = left ? "V" : "F";
        String r = right ? "V" : "F";
        String res = result ? "VERDADEIRO" : "FALSO";

        return switch (modoAtual) {
            case PROFESSOR -> getProfessorExplanation(op, l, r, res, left, right);
            case POPULAR   -> getPopularExplanation(op, l, r, res, left, right);
            case FORMAL    -> "A operação lógica '" + op + "' aplicada aos operandos " + l + " e " + r + " resulta em " + res + ".";
            default        -> "Operador " + op + " com entradas " + l + " e " + r + " resulta em " + res + ".";
        };
    }
    private String getProfessorExplanation(String op, String l, String r, String res, boolean lv, boolean rv) {
        return switch (op) {
            case "^" -> (lv && rv) ? "Como ambos os lados são verdadeiros, o 'E' confirma a verdade." : "O 'E' exige que ambos sejam verdadeiros, mas como temos um Falso, o resultado é Falso.";
            case "v" -> (lv || rv) ? "No 'OU', basta que um dos lados seja verdadeiro para que tudo seja verdade." : "Como não há nenhuma verdade em nenhum dos lados, o 'OU' resulta em Falso.";
            case "->" -> (!lv || rv) ? "Na implicação, só seria falso se tivéssemos V indo para F. Como não é o caso, temos uma verdade." : "A promessa foi quebrada! Um verdadeiro levando a um falso torna a implicação Falsa.";
            case "~" -> "A negação funciona como um espelho: ela inverte o valor de " + r + " para " + res + ".";
            case "v_excl" -> (!lv && !rv || lv && rv) ? "Como os valores são iguais, a condição de exclusividade falha, resultando em Falso." : "Os valores são distintos, o que satisfaz a exclusividade do XOR, resultando em Verdadeiro.";
            default -> "Analisando o operador " + op + ", obtemos o valor " + res + ".";
        };
    }

    private String getPopularExplanation(String op, String l, String r, String res, boolean lv, boolean rv) {
        return switch (op) {
            case "^" -> (lv && rv) ? "Os dois batem, então é V!" : "Um deles deu F, aí o 'E' quebra e dá F.";
            case "v" -> (lv || rv) ? "Teve um verdadeiro no meio? Então o 'OU' aceita!" : "Tudo falso? Aí o 'OU' não tem como te ajudar.";
            case "->" -> "Essa seta diz que se o primeiro for V, o segundo tem que ser também. Deu " + res + ".";
            case "~" -> "O sinal de negação trocou tudo: era " + r + " e virou " + res + ".";
            case "v_excl" -> (lv ^ rv) ? "Aqui é ou um, ou outro! Como só tem um verdadeiro, o resultado é V." : "Os dois iguais? Aí o OU exclusivo não aceita, tem que ser diferente pra dar V!";
            default -> "Esse " + op + " resultou em " + res + ".";
        };
    }

    public String generateIntro() {

        String intros[] = {
                "Note que,",
                "Pense da seguinte forma:",
                "Olha só o esquema:",
                "Verifica-se, portanto,"
        };

        int pesos[] = switch(modoAtual) {
            case PADRAO    -> new int[]{40, 30, 20, 10};
            case PROFESSOR -> new int[]{15, 60, 15, 10};
            case POPULAR   -> new int[]{10, 10, 70, 10};
            case FORMAL    -> new int[]{10, 10, 10, 70};
        };
        return pickWeighted(intros, pesos);
    }

    private String pickWeighted(String opcoes[], int pesos[]) {
        int total = 0;
        for (int p : pesos) total += p;

        int val = random.nextInt(total);
        int acumulado = 0;

        for (int i = 0; i < opcoes.length; i++) {
            acumulado += pesos[i];
            if (val < acumulado) {
                return opcoes[i];
            }
        }
        return opcoes[0];
    }

    public String generateFinalResult(String classificacao) {
        return switch(classificacao) {
            case "TAUTOLOGIA" -> switch(modoAtual) {
                case PROFESSOR -> "Isso é uma Tautologia. Imagine uma verdade absoluta: não importa o que aconteça, o resultado será sempre positivo.";
                case POPULAR   -> "É Tautologia, o sistema tá blindado, não tem erro, é verdade do começo ao fim!";
                case FORMAL    -> "Conclui-se tratar de uma Tautologia, dada a invariabilidade do valor lógico em todas as interpretações.";
                default        -> "O resultado final é uma Tautologia. A expressão é sempre verdadeira para qualquer entrada.";
            };
            case "CONTRADIÇÃO" -> switch(modoAtual) {
                case PROFESSOR -> "Temos uma Contradição. É como uma conta que nunca fecha, a própria lógica se anula e o resultado é sempre falso.";
                case POPULAR   -> "Deu ruim, é Contradição! A lógica se atropelou toda e não sobra uma verdade pra contar história.";
                case FORMAL    -> "Verifica-se uma Contradição, caracterizada pela falsidade estrita em todo o domínio da tabela.";
                default        -> "Esta é uma Contradição. A fórmula resulta em falso em todos os cenários possíveis.";
            };
            default -> switch (modoAtual) {
                case PROFESSOR -> "Isso é uma Contingência. Ou seja, o resultado depende dos valores, pode ser verdadeiro ou falso.";
                case POPULAR   -> "É Contingência! Tá em cima do muro, ás vezes é V e ás vezes é F, depende do dia!";
                case FORMAL    -> "A presente fórmula configura uma Contingência, apresentando valores lógicos distintos conforme a variação das instâncias.";
                default        -> "O resultado é uma Contingência. A validade da expressão varia conforme as entradas.";
            };
        };
    }

    public int changeMode(String input) {
        String texto = input.toLowerCase();

        if (input.contains("ajuda") || input.contains("entendi") || input.contains("explica de novo") || input.contains("explicar de novo") || input.contains("nao entendi") || input.contains("didatico")) {
            setModoAtual(Modo.PROFESSOR);
        }
        else if (input.contains("popular") || input.contains("simples") || input.contains("informal") || input.contains("salve")) {
            setModoAtual(Modo.POPULAR);
        }
        else if (input.contains("formal") || input.contains("rebuscada")) {
            setModoAtual(Modo.FORMAL);
        }
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(texto);

        if (m.find()) {
            return Integer.parseInt(m.group()) - 1;
        }
        return -1;
    }
    public void falar(String texto) {
        for (char c : texto.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }
}
