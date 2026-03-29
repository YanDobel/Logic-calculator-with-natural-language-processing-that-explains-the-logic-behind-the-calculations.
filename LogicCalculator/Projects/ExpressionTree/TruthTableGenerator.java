package Projects.ExpressionTree;
import java.util.*;
import Trees.AVL.AVL_SucDireito.*;
import LinkedList.LinkedList;

public class TruthTableGenerator {
    private ShuntingYard parser = new ShuntingYard();
    private ExpressionTree tree = new ExpressionTree();
    private String output;

    public LogicResult prove(String expression) {
        LinkedList<String> postfix = parser.shuntingYard(expression);
        LinkedList<String> variables = extractVariables(postfix);
        tree.buildPostFix(postfix);

        LinkedList<Node> subNodes = new LinkedList<Node>();
        collectSubExpressions(tree.getRoot(), new HashSet<>(), subNodes);

        int l = 0;
        l += variables.size() * 4;
        StringBuilder sb = new StringBuilder();
        StringifierVisitor strVisitor = new StringifierVisitor();

        for (String var : variables) {
            sb.append(var).append(" | ");
        }

        for (Node sub : subNodes) {
            l += sub.accept(strVisitor).length() + 3;
            sb.append(sub.accept(strVisitor)).append(" | ");
        }
        l += 5;

        sb.append("FINAL\n").append("-".repeat(l)).append("\n");

        int rows = (int) Math.pow(2, variables.size());
        boolean results[] = new boolean[rows];
        boolean hasTrue = false, hasFalse = false;

        for (int i = 0; i < rows; i++) {
            Map<String, Boolean> values = new HashMap<>();
            for (int j = 0; j < variables.size(); j++) {
                boolean val = ((i >> (variables.size() - 1 - j) & 1) == 1);
                values.put(variables.get(j), val);
                sb.append(val ? "V" : "F").append(" | ");
            }

            EvaluateVisitor evalVisitor = new EvaluateVisitor(values);

            for (Node sub : subNodes) {
                boolean subRes = sub.accept(evalVisitor);
                sb.append(subRes ? "V" : "F").append(" | ");
            }

            boolean finalRes = tree.getRoot().accept(evalVisitor);
            results[i] = finalRes;
            sb.append(finalRes ? "V" : "F").append("\n");

            if (finalRes) {
                hasTrue = true;
            } else {
                hasFalse = true;
            }
        }
        return new LogicResult(sb.toString(), classify(hasTrue, hasFalse), variables, results);
    }

    public static class StepData {
        public String op;
        public boolean leftVal;
        public boolean rightVal;
        public boolean res;

        public StepData(String op, boolean leftVal, boolean rightVal, boolean res) {
            this.op = op;
            this.leftVal = leftVal;
            this.rightVal = rightVal;
            this.res = res;
        }
    }

    private void collectSubExpressions(Node node, Set<String> seen, LinkedList<Node> list) {
        if (node == null || node instanceof VariableNode) return;

        if (node.getLeft() != null) collectSubExpressions(node.getLeft(), seen, list);
        if (node.getRight() != null) collectSubExpressions(node.getRight(), seen, list);

        StringifierVisitor strVisitor = new StringifierVisitor();
        String representation = node.accept(strVisitor);

        if (!seen.contains(representation)) {
            seen.add(representation);
            list.add(node);
        }
    }

    private void collectData(Node node, Map<String, Boolean> values, LinkedList<StepData> steps) {
        if (node == null || node instanceof VariableNode) return;

        collectData(node.getLeft(), values, steps);
        collectData(node.getRight(), values, steps);

        EvaluateVisitor eval = new EvaluateVisitor(values);

        boolean lV = (node.getLeft() != null) && node.getLeft().accept(eval);
        boolean rV = node.getRight().accept(eval);
        boolean res = node.accept(eval);

        steps.add(new StepData(node.getValue(), lV, rV, res));
    }

    public LinkedList<StepData> ask(int lineIndex, String formula) {
        LinkedList<String> postfix = parser.shuntingYard(formula);
        LinkedList<String> variables = extractVariables(postfix);
        tree.buildPostFix(postfix);

        Map<String, Boolean> values = new HashMap<>();
        for (int j = 0; j < variables.size(); j++) {
            boolean val = ((lineIndex >> (variables.size() - 1 - j) & 1) == 1);
            values.put(variables.get(j), val);
        }

        LinkedList<StepData> explanationSteps = new LinkedList<StepData>();
        collectData(tree.getRoot(), values, explanationSteps);
        return explanationSteps;
    }

    private LinkedList<String> extractVariables(LinkedList<String> postfix) {
       AVL<String> avl = new AVL<String>();

       for (String s : postfix) {
           if (s.matches("[a-zA-Z0-9]+") && !ShuntingYard.isOperator(s)) {
                if (!avl.contains(s)) avl.add(s);
           }
       }
       return avl.inOrderLinkedList();
    }

    private String classify(boolean hasTrue, boolean hasFalse) {
        if (hasTrue && !hasFalse) return "TAUTOLOGIA";
        if (!hasTrue && hasFalse) return "CONTRADIÇÃO";
        return "CONTINGÊNCIA";
    }

    public boolean isArgumentValid(LinkedList<String> premissas, String conclusao) {
        LinkedList<ExpressionTree> trees = new LinkedList<ExpressionTree>();

        for (String s : premissas) {
            ExpressionTree t = new ExpressionTree();
            t.buildPostFix(parser.shuntingYard(s));
            trees.add(t);
        }

        ExpressionTree cTree = new ExpressionTree();
        cTree.buildPostFix(parser.shuntingYard(conclusao));

        AVL<String> avl = new AVL<String>();

        for (String s : premissas) {
            LinkedList<String> postfix = parser.shuntingYard(s);
            for (String v : extractVariables(postfix)) {
                avl.add(v);
            }
        }
        for (String a : extractVariables(parser.shuntingYard(conclusao))) {
            avl.add(a);
        }
        LinkedList<String> variables = avl.inOrderLinkedList();

        int rows = (int) Math.pow(2, variables.size());

        for (int i = 0; i < rows; i++) {
            Map<String, Boolean> values = new HashMap<>();
            for (int j = 0; j < variables.size(); j++) {
                boolean val = ((i >> (variables.size() - 1 - j) & 1) == 1);
                values.put(variables.get(j), val);
            }

            EvaluateVisitor eval = new EvaluateVisitor(values);

            boolean tudoCerto = true;
            for (ExpressionTree t : trees) {
                if (!t.getRoot().accept(eval)) {
                    tudoCerto = false;
                    break;
                }
            }
            if (tudoCerto) {
                boolean conclusionVal = cTree.getRoot().accept(eval);
                if (!conclusionVal) {
                    return false;
                }
            }
        }
        return true;
    }
}