package ale.exptree.java.controller;

import ale.exptree.java.ExpressionEvaluator;
import ale.exptree.java.ExpressionTree;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TextField tfExpression;

    @FXML
    private Label labResultado;

    @FXML
    private Button bGenerate;

    @FXML
    private Canvas canvas;

    private final int PARENT_TO_CHILD = 40;
    private final int CHILD_TO_CHILD = 20;
    private final int FONT_SIZE = 18;
    private final int NODE_WIDTH = 25;
    private final int NODE_HEIGHT = 20;
    private final Color NODE_COLOR = Color.BLACK;
    private final Color FONT_COLOR = Color.WHITE;
    private final Color LINE_COLOR = Color.BLACK;

    private GraphicsContext graphics;
    private ExpressionTree tree;
    private Map<ExpressionTree.Node, Rectangle> nodePositions = new HashMap<>();
    private Map<ExpressionTree.Node, Dimension> subtreeSizes = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphics = canvas.getGraphicsContext2D();
        graphics.moveTo(canvas.getWidth()/2, 20);

        bGenerate.setOnAction(bGenerateOnAction);
    }

    private final EventHandler<ActionEvent> bGenerateOnAction = event -> {
        String expression = tfExpression.getText();

        // Calculate result
        try {
            int result = ExpressionEvaluator.evaluate(expression);
            labResultado.setText("Resultado: " + result);
        } catch (Exception ignored) { }

        // Draw expression tree
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        tree = new ExpressionTree(expression);
        calculatePositions();
        drawExpressionTree(tree.getRoot(), Integer.MAX_VALUE, Integer.MAX_VALUE, 10);
    };

    private void drawExpressionTree(ExpressionTree.Node root, int x, int y, int offset) {
        if (root == null)
            return;

        int XOFFSET = (int) canvas.getWidth()/2;

        Rectangle rect = nodePositions.get(root);

        graphics.setFill(NODE_COLOR);
        graphics.fillRect(rect.x + XOFFSET, rect.y, rect.width, rect.height);

        graphics.setFill(FONT_COLOR);
        graphics.setFont(new Font(FONT_SIZE));
        graphics.fillText(root.value, rect.x + XOFFSET + 7, rect.y + offset + 5);

        graphics.setFill(LINE_COLOR);
        if (x != Integer.MAX_VALUE)
            graphics.strokeLine(x + XOFFSET, y, rect.x + rect.width/2 + XOFFSET, rect.y);

        drawExpressionTree(root.left, rect.x + rect.width/2, rect.y + rect.height, offset);
        drawExpressionTree(root.right, rect.x + rect.width/2, rect.y + rect.height, offset);
    }

    private void calculatePositions() {
        nodePositions.clear();
        subtreeSizes.clear();

        ExpressionTree.Node root = tree.getRoot();

        if (root == null)
            return;

        calculateSubtreeSizes(root);
        calculateNodePositions(root, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
    }

    private Dimension calculateSubtreeSizes(ExpressionTree.Node root) {
        if (root == null)
            return new Dimension(0, 0);

        Dimension leftDimension = calculateSubtreeSizes(root.left);
        Dimension rightDimension = calculateSubtreeSizes(root.right);

        int width = leftDimension.width + CHILD_TO_CHILD + rightDimension.width;
        //int height = fontMetrics.getHeight() + PARENT_TO_CHILD + Math.max(leftDimension.height, rightDimension.height);
        int height = PARENT_TO_CHILD + Math.max(leftDimension.height, rightDimension.height);

        var dimension = new Dimension(width, height);
        subtreeSizes.put(root, dimension);

        return dimension;
    }

    private void calculateNodePositions(ExpressionTree.Node root, int left, int right, int top) {
        if (root == null)
            return;

        Dimension leftDimension = subtreeSizes.get(root.left);
        if (leftDimension == null)
            leftDimension = new Dimension(0, 0);

        Dimension rightDimension = subtreeSizes.get(root.right);
        if (rightDimension == null)
            rightDimension = new Dimension(0, 0);

        int center = 0;

        if (right != Integer.MAX_VALUE)
            center = right - rightDimension.width - CHILD_TO_CHILD/2;

        else if (left != Integer.MAX_VALUE)
            center = left + leftDimension.width + CHILD_TO_CHILD/2;

        //int width = fontMetrics.stringWidth(root.value);
        int width = 10;

        //nodePositions.put(root, new Rectangle(center - width/2 - 3, top, width + 6, fontMetrics.getHeight()));
        //nodePositions.put(root, new Rectangle(center - width/2 - 3, top, width + 6, 10));
        nodePositions.put(root, new Rectangle(center - width/2 - 3, top, NODE_WIDTH, NODE_HEIGHT));

        //calculateNodePositions(root.left, Integer.MAX_VALUE, center - CHILD_TO_CHILD/2, top + fontMetrics.getHeight() + PARENT_TO_CHILD);
        //calculateNodePositions(root.right, center + CHILD_TO_CHILD/2, Integer.MAX_VALUE, top + fontMetrics.getHeight() + PARENT_TO_CHILD);

        calculateNodePositions(root.left, Integer.MAX_VALUE, center - CHILD_TO_CHILD/2, top + PARENT_TO_CHILD);
        calculateNodePositions(root.right, center + CHILD_TO_CHILD/2, Integer.MAX_VALUE, top + PARENT_TO_CHILD);
    }
}
