package Projects.ExpressionTree;

public class StringifierVisitor implements NodeVisitor<String> {
    @Override
    public String visit(VariableNode node) {
        return node.getValue();
    }

    @Override
    public String visit(BinaryOperatorNode node) {
        return "(" + node.getLeft().accept(this) + " " + node.getValue() + " " + node.getRight().accept(this) + ")";
    }

    @Override
    public String visit(UnaryOperatorNode node) {
        return node.getValue() + node.getRight().accept(this);
    }
}
