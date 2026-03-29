package Projects.ExpressionTree;
import LinkedList.LinkedList;

public class ArgumentProcessor {
    private ShuntingYard parser = new ShuntingYard();
    private final TruthTableGenerator generator = new TruthTableGenerator();

    public boolean process(String input) {
        String premissasRaw = input.substring(input.indexOf("{") + 1, input.indexOf("}"));
        String partes[] = input.split("\\|=\\s*|⊨\\s*");
        if (partes.length < 2) {
            throw new RuntimeException("(!) Símbolo de consequência não encontrado (!)");
        }
        String conclusao = partes[1].trim();

        String[] cadaPremissa = premissasRaw.split(",");
        LinkedList<String> listaPremissas = new LinkedList<String>();
        for (String p : cadaPremissa) {
            listaPremissas.add(p.trim());
        }
        return generator.isArgumentValid(listaPremissas, conclusao);
    }
}
