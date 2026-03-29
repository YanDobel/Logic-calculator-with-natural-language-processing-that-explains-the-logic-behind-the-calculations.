package Projects.ExpressionTree;
import java.util.*;

public class EvaluateVisitor implements NodeVisitor<Boolean> {
    private final Map<String, Boolean> values;

    public EvaluateVisitor(Map<String, Boolean> values) {
        this.values = values;
    }

    @Override
    public Boolean visit(VariableNode node) {
        return values.getOrDefault(node.getValue(), false);
    }

    @Override
    public Boolean visit(BinaryOperatorNode node) {
        boolean left = node.getLeft().accept(this);
        boolean right = node.getRight().accept(this);

        return switch (node.getValue()) {
            case "^" -> left && right;
            case "v" -> left || right;
            case "->" -> !left || right;
            case "<->" -> left == right;
            case "v_excl" -> left ^ right;
            default -> throw new RuntimeException("(!) Operador desconhecido: " + node.getValue() + " (!)");
        };
    }

    @Override
    public Boolean visit(UnaryOperatorNode node) {
        boolean operand = node.getRight().accept(this);
        return node.getValue().equals("~") ? !operand : operand;
    }
}
