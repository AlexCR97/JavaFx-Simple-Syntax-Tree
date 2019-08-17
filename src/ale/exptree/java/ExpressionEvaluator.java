package ale.exptree.java;

import java.util.Stack;

public class ExpressionEvaluator {

    public static int evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        var values = new Stack<Integer>();
        var operators = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++) {

            if (tokens[i] == ' ')
                continue;

            if (tokens[i] >= '0' && tokens[i] <= '9') {
                var stringBuilder = new StringBuilder();

                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9')
                    stringBuilder.append(tokens[i++]);

                values.push(Integer.parseInt(stringBuilder.toString()));
            }

            else if (tokens[i] == '(')
                operators.push(tokens[i]);

            else if (tokens[i] == ')') {
                while (operators.peek() != '(')
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                operators.pop();
            }

            else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                while (!operators.empty() && hasPrecedence(tokens[i], operators.peek()))
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));

                operators.push(tokens[i]);
            }
        }

        while (!operators.empty())
            values.push(applyOperation(operators.pop(), values.pop(), values.pop()));

        return values.pop();
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;

        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    private static int applyOperation(char op, int b, int a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0)
                    throw new UnsupportedOperationException("Cannot divide by zero");

                return a / b;
        }
        return -1;
    }

}
