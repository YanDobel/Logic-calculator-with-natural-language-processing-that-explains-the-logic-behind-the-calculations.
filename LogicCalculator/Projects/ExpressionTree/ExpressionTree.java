package Projects.ExpressionTree;
import LinkedList.LinkedList;
import java.util.*;

public class ExpressionTree {
    private Node root;

    public Node getRoot() {
        return this.root;
    }

    public void buildPostFix(LinkedList<String> postfix) {
        LinkedList<Node> stack = new LinkedList<Node>();

        for (String token : postfix) {
            if (ShuntingYard.isOperator(token)) {
                if (token.equals("~")) {
                    if (stack.isEmpty()) {
                        throw new RuntimeException("(!) Erro sintático: operador sem operandos (!)");
                    }
                    Node child = stack.pop();
                    stack.push(new UnaryOperatorNode(token, child));
                } else {
                    if (stack.size() < 2) {
                        throw new RuntimeException("(!) Erro sintático: falta operando para " + token + " (!)");
                    }
                    Node right = stack.pop();
                    Node left = stack.pop();
                    stack.push(new BinaryOperatorNode(token, left, right));
                }
            } else {
                stack.push(new VariableNode(token));
            }
        }
        if (stack.size() != 1) {
            throw new RuntimeException("(!) A construção não é possível: faltam conectivos entre proposições (!)");
        }
        this.root = stack.pop();
    }


    public boolean evaluate(Node node, Map<String, Boolean> values) {
        if (root == null) return false;
        EvaluateVisitor visitor = new EvaluateVisitor(values);
        return root.accept(visitor);
    }

    public String toString() {
        if (root == null) return "";
        StringifierVisitor visitor = new StringifierVisitor();
        return root.accept(visitor);
    }
}