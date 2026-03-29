package Projects.ExpressionTree;
import LinkedList.LinkedList;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TruthTableGenerator generator = new TruthTableGenerator();
        WatsonEngine watson = new WatsonEngine();

        while (true) {
            watson.falar("\nOlá, digite uma expressão que deseja analisar (ou 'sair' para encerrar): ");
            String formula = sc.nextLine();

            if (formula.equalsIgnoreCase("sair")) break;

            if (formula.contains("{") && (formula.contains("|="))) {
                ArgumentProcessor argProc = new ArgumentProcessor();
                boolean eValido = argProc.process(formula);
                watson.falar(eValido ? "O argumento é VÁLIDO!" : "O argumento é INVÁLIDO (falácia detected)!");
                continue;
            }

            try {
                LogicResult resultado = generator.prove(formula);
                System.out.println("\n" + resultado.getTable());
                watson.falar("\nConclusão final da expressão:");
                watson.falar(watson.generateFinalResult(resultado.getClassification()));

                int memoriaContexto = -1;

                while (true) {
                    watson.falar("\nDeseja que eu explique alguma linha ou o resultado? ");
                    String entrada = sc.nextLine().toLowerCase();

                    if (entrada.contains("nova") || entrada.contains("outra") || entrada.contains("mais uma") || entrada.equals("nao") || entrada.equals("não")) {
                        break;
                    }

                    if (entrada.contains("de novo") || entrada.contains("repetir") || entrada.contains("novamente")) {
                        if (memoriaContexto == -2) {

                            entrada += " resultado";
                        } else if (memoriaContexto != -1) {

                        } else {
                            watson.falar("Eu ainda não expliquei nada para poder repetir!");
                            continue;
                        }
                    }

                    if (entrada.contains("resultado") || entrada.contains("veredito") || entrada.contains("conclusão")) {
                        watson.changeMode(entrada);
                        memoriaContexto = -2;
                        watson.falar("\n== Analisando o Resultado Geral ==");
                        watson.falar(watson.generateFinalResult(resultado.getClassification()));
                        continue;
                    }

                    int linha = watson.changeMode(entrada);

                    if (linha == -1 && memoriaContexto >= 0 && (entrada.contains("de novo") || entrada.contains("repetir"))) {
                        linha = memoriaContexto;
                    }

                    if (linha != -1) {
                        if (linha >= 0 && linha < resultado.results.length) {
                            memoriaContexto = linha;
                            watson.falar("\n== Explicando a linha " + (linha + 1) + " ==");
                            LinkedList<TruthTableGenerator.StepData> passos = generator.ask(linha, formula);
                            for (TruthTableGenerator.StepData dado : passos) {
                                watson.falar(watson.explainLine(dado.op, dado.leftVal, dado.rightVal, dado.res));
                            }
                        } else {
                            watson.falar("Opa, essa tabela só vai da linha 1 até a linha " + resultado.results.length + ".");
                        }
                    } else {
                        watson.falar("Não encontrei o número da linha. Pode me dizer qual linha você quer?");
                    }
                }
            } catch (Exception e) {
                watson.falar("Ops, parece que houve um erro com a sua expressão: " + e.getMessage());
            }
        }
        watson.falar("Encerrando o sistema. Até logo!");
    }
}