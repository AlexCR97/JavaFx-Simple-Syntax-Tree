package ale.exptree.java;

import java.util.ArrayList;
import java.util.Stack;

public class InfixToPostfix {

    public static ArrayList<String> convert(String infixExpression) {

        System.out.println("Expression is: " + infixExpression);

        var infixTokens = new ArrayList<String>();

        // Get infix tokens
        for (int i = 0; i < infixExpression.length(); i++) {

            System.out.println("Current character is: " + infixExpression.charAt(i));

            if (infixExpression.charAt(i) == ' ')
                continue;

            // current character is number
            if (infixExpression.charAt(i) >= '0' && infixExpression.charAt(i) <= '9') {

                var number = new StringBuilder();

                while (i < infixExpression.length() && infixExpression.charAt(i) >= '0' && infixExpression.charAt(i) <= '9')
                    number.append(infixExpression.charAt(i++));

                infixTokens.add(number.toString());
            }

            else
                infixTokens.add(String.valueOf(infixExpression.charAt(i)));
        }

        System.out.println("Infix tokens are: " + infixTokens);

        var postfixTokens = new ArrayList<String>();
        var tokenStack = new Stack<String>();

        for (String currentToken : infixTokens) {

            if (getPrecedence(currentToken) > 0) {
                while (!tokenStack.isEmpty() && getPrecedence(tokenStack.peek()) >= getPrecedence(currentToken))
                    postfixTokens.add(tokenStack.pop());

                tokenStack.push(currentToken);
            }
            else if (currentToken.equals(")")) {
                String x = tokenStack.pop();
                while (!x.equals("(")) {
                    postfixTokens.add(x);
                    x = tokenStack.pop();
                }
            }
            else if (currentToken.equals("(")) {
                tokenStack.push(currentToken);
            }
            else {
                postfixTokens.add(currentToken);
            }
        }

        for (int i = 0; i <= tokenStack.size(); i++) {
            postfixTokens.add(tokenStack.pop());
        }

        System.out.println("Postfix tokens are: " + postfixTokens);

        return postfixTokens;
    }

    private static int getPrecedence(String operator) {
        switch (operator) {
            case "+": return 1;
            case "-": return 1;
            case "*": return 2;
            case "/": return 2;
            case "^": return 3;
            default: return -1;
        }
    }

}
