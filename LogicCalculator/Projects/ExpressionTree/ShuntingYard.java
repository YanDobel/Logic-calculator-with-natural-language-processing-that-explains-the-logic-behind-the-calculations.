package Projects.ExpressionTree;
import LinkedList.LinkedList;
import java.util.*;
import java.util.regex.*;

public class ShuntingYard {
    private static final Map<String, Integer> PRECEDENCE = Map.of(
            "~", 3,
            "^", 2,
            "v", 1,
            "->",1,
            "<->",1,
            "v_excl",1
    );

    private LinkedList<String> prepareTokens(String expression) {
        String regex = "(<->|->|v_excl|\\|=|[{} ,()~^v]|[a-zA-Z0-9]+)";
        Matcher matcher = Pattern.compile(regex).matcher(expression);

        LinkedList<String> tokensList = new LinkedList<String>();

        while (matcher.find()) {
            tokensList.add(matcher.group());
        }
        return tokensList.map(String::trim).filter(s -> !s.isEmpty()
                && !s.equals(",") && !s.equals(" ") && !s.equals("{") && !s.equals("}"));
    }

    public LinkedList<String> shuntingYard(String expression) {

        LinkedList<String> tokens = prepareTokens(expression);
        LinkedList<String> output = new LinkedList<String>();
        LinkedList<String> stack = new LinkedList<String>();
        String lastToken = null;

        for (String token : tokens) {
            if (isBinaryOperator(token) && isBinaryOperator(lastToken)) {
                throw new RuntimeException("(!) A construção não é possível: conectivos consecutivos detectados (!)");
            }
            if (isBinaryOperator(token)) {
                    if (lastToken == null || lastToken.equals("(") || isBinaryOperator(lastToken)) {
                    throw new RuntimeException("(!) A construção não é possível: operador binário sem operando à esquerda (!)");
                }
            }
            if (isOperand(token)) {
                output.add(token);
            }
            else if (token.equals("(")) {
                stack.push(token);
            }
            else if (token.equals(")")) {
                if (stack.isEmpty()) {
                    throw new RuntimeException("(!) A construção não é possível: parênteses fechado sem ser aberto (!)");
                }
                while (!stack.isEmpty() && !stack.peak().equals("(")) {
                    output.add(stack.pop());
                }
                if (stack.isEmpty()) {
                    throw new RuntimeException("(!) A construção não é possível: parênteses mal posicionados (!)");
                }
                stack.pop();
            }
            else if (isOperator(token)) {
                while (!stack.isEmpty()
                        && !stack.peak().equals("(")
                        && PRECEDENCE.getOrDefault(stack.peak(), 0)
                        >= PRECEDENCE.getOrDefault(token, 0))
                {
                    if (stack.peak().equals("(")) {
                        throw new RuntimeException("(!) A construção não é possível: parênteses não fechados (!)");
                    }
                    output.add(stack.pop());
                }
                stack.push(token);
            }
            lastToken = token;
        }
        if (isBinaryOperator(lastToken) || (lastToken != null && lastToken.equals("~"))) {
            throw new RuntimeException("(!) A construção não é possível: fórmula terminada em operador (!)");
        }

        while (!stack.isEmpty()) {
            String aux = stack.pop();
            if (aux.equals("(")) {
                throw new RuntimeException("(!) A construção não é possível: parênteses aberto que nunca foi fechado (!)");
            }
            output.add(aux);
        }
        return output;
    }

    private static boolean isOperand(String token) {
        return !PRECEDENCE.containsKey(token)
                && !token.equals("(")
                && !token.equals(")");
    }

    private static boolean isBinaryOperator(String token) {
        if (token == null) return false;
        return token.equals("^") || token.equals("v") || token.equals("->")
                || token.equals("<->") || token.equals("v_excl");
    }

    public static boolean isOperator(String token) {
        return PRECEDENCE.containsKey(token);
    }
}