package ale.exptree.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ExpressionTree {

    public static class Node {
        public String value;
        public Node left;
        public Node right;

        public Node() {}

        public Node(String value) {
            this.value = value;
        }

        public Node(String value, Node left, Node right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            var rootValue = (value == null)? "null" : value;
            var leftValue = (left == null)? "null" : left.value;
            var rightValue = (right == null)? "null" : right.value;

            return leftValue + " <- " + value + " -> " + rightValue;
        }
    }

    private Node root;
    public Node getRoot() {
        return root;
    }

    public ExpressionTree(String expression) {
        String postfix = infixToPostfix(expression);
        root = constructTree(postfix);
    }

    private Node constructTree(String postfix) {
        char[] tokens = postfix.toCharArray();
        var stack = new Stack<Node>();
        Node t, t1, t2;

        for (char token : tokens) {

            t = new Node(String.valueOf(token));

            if (!isOperator(token))
                stack.push(t);

            else {
                t1 = stack.pop();
                t2 = stack.pop();

                t.right = t1;
                t.left = t2;

                stack.push(t);
            }
        }

        t = stack.peek();
        stack.pop();

        return t;
    }

    public List<Node> traverseInorder() {
        var nodes = new ArrayList<Node>();
        traverseInorder(root, nodes);
        return nodes;
    }

    private void traverseInorder(Node root, List<Node> nodes) {
        if (root != null) {
            traverseInorder(root.left, nodes);
            nodes.add(root);
            traverseInorder(root.right, nodes);
        }
    }

    public List<Node> traversePostorder() {
        var nodes = new ArrayList<Node>();
        traversePostorder(root, nodes);
        return nodes;
    }

    private void traversePostorder(Node root, List<Node> nodes) {
        if (root != null) {
            traversePostorder(root.left, nodes);
            traversePostorder(root.right, nodes);
            nodes.add(root);
        }
    }

    public List<Node> traversePreorder() {
        var nodes = new ArrayList<Node>();
        traversePreorder(root, nodes);
        return nodes;
    }

    private void traversePreorder(Node root, List<Node> nodes) {
        if (root != null) {
            nodes.add(root);
            traversePreorder(root.left, nodes);
            traversePreorder(root.right, nodes);
        }
    }

    private boolean isOperator(char token) {
        return token == '+' || token == '-' || token == '*' || token == '/' || token == '^';
    }

    private String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < infix.length(); i++) {
            char currentToken = infix.charAt(i);

            if (getPrecedence(currentToken) > 0) {
                while(!stack.isEmpty() && getPrecedence(stack.peek()) >= getPrecedence(currentToken))
                    postfix.append(stack.pop());

                stack.push(currentToken);
            }
            else if (currentToken == ')') {
                char x = stack.pop();
                while (x != '(') {
                    postfix.append(x);
                    x = stack.pop();
                }
            }
            else if (currentToken == '(') {
                stack.push(currentToken);
            }
            else {
                postfix.append(currentToken);
            }
        }

        for (int i = 0; i <= stack.size(); i++) {
            postfix.append(stack.pop());
        }

        return postfix.toString();
    }

    private int getPrecedence(char operator) {
        switch (operator) {
            case '+': return 1;
            case '-': return 1;
            case '*': return 2;
            case '/': return 2;
            case '^': return 3;
            default: return -1;
        }
    }

}
