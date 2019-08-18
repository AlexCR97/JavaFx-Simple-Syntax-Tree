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

            return leftValue + " <- " + rootValue + " -> " + rightValue;
        }
    }

    private Node root;
    public Node getRoot() {
        return root;
    }

    public ExpressionTree(String expression) {
        ArrayList<String> postfixTokens = InfixToPostfix.convert(expression);
        root = constructTree(postfixTokens);
    }

    private Node constructTree(ArrayList<String> tokens) {
        var stack = new Stack<Node>();
        Node t, t1, t2;

        for (String token : tokens) {

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

    private boolean isOperator(String token) {
        return token.equals("+") ||token.equals("-") ||token.equals("*") ||token.equals("/") ||token.equals("^");
    }

}
