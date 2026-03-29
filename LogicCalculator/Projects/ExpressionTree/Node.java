package Projects.ExpressionTree;

public abstract sealed class Node permits VariableNode, BinaryOperatorNode, UnaryOperatorNode {
    private String value;
    private Node right;
    private Node left;
    private Node parent;

    public Node(String value) {
        this.value = value;
    }

    public abstract <T> T accept(NodeVisitor<T> visitor);

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
        if (right != null) right.parent = this;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
        if (left != null) left.parent = this;
    }
}

final class VariableNode extends Node {
    public VariableNode(String value) {
        super(value);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

final class BinaryOperatorNode extends Node {
    public BinaryOperatorNode(String value, Node left, Node right) {
        super(value);
        setLeft(left);
        setRight(right);
    }
    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

final class UnaryOperatorNode extends Node {
    public UnaryOperatorNode(String value, Node right) {
        super(value);
        setRight(right);
    }
    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}