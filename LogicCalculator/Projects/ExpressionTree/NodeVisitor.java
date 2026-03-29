package Projects.ExpressionTree;

public interface NodeVisitor<T> {
    T visit(BinaryOperatorNode node);
    T visit(UnaryOperatorNode node);
    T visit(VariableNode node);
}
