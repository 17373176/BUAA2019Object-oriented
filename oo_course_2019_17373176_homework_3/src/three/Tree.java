package three;

import java.math.BigInteger;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tree {
    private String string = "";
    private Node root;     //根节点

    public String create(String str) { // create three
        Stack<Node> poly = new Stack<Node>(); // tree stack
        Stack<String> op = new Stack<String>(); // op stack
        node(str, poly, op);
        root = poly.peek(); // root node
        //return resovle(root).replaceAll("1\\*", ""); //求导
        return resovle(root);
    }

    public void node(String str, Stack<Node> poly, Stack<String> op) {
        String term = "(\\d+|x|sin|cos|\\+|-|\\*|\\^|\\(|\\))";
        Pattern match = Pattern.compile(term);
        Matcher multi = match.matcher(str);
        String s = str;
        while (multi.lookingAt()) {
            String string = multi.group();
            s = s.replaceFirst(term, "");
            multi = match.matcher(s);
            if (string.equals("(")) {
                op.push(string); // 符号入栈
            } else if (isOp(string)) {
                if (!op.empty()) {
                    String top = op.peek();
                    while (getPriority(string, top) < 0) { //优先级小一直出栈
                        String s1 = op.pop();
                        Node right = poly.pop();
                        Node left = poly.pop();
                        poly.push(new Node(s1, left, right));
                        if (!op.empty()) {
                            top = op.peek();
                        } else {
                            break;
                        }
                    }
                }
                op.push(string);
            } else if (string.equals(")")) {
                String top = op.peek();
                while (!top.equals("(")) {
                    String s1 = op.pop();
                    Node right = poly.pop();
                    Node left = poly.pop();
                    poly.push(new Node(s1, left, right));
                    if (!op.empty()) {
                        top = op.peek();
                    } else {
                        break;
                    }
                }
                op.pop();
            } else if (isX(string)) {
                poly.push(new Node(string, null, null));
                if (poly.peek().getData().equals("sin")) {
                    op.push("s");
                } else if (poly.peek().getData().equals("cos")) {
                    op.push("c");
                }
            }
        }
        while (!op.empty()) {
            String s1 = op.pop();
            Node right = poly.pop();
            Node left = poly.pop();
            poly.push(new Node(s1, left, right));
        }
    }

    public String resovle(Node tree) {
        Polyterm factor = new Polyterm();
        if (tree.getLchild() == null && tree.getRchild() == null) {
            return factor.Polyterm(tree.getData());
        }
        if (tree.getData().equals("+")) {
            return "(" + resovle(tree.getLchild()) + "+" +
                    resovle(tree.getRchild()) + ")";
        } else if (tree.getData().equals("-")) {
            return "(" + resovle(tree.getLchild()) + "-" +
                    resovle(tree.getRchild()) + ")";
        } else if (tree.getData().equals("*")) {
            return "(" + resovle(tree.getLchild()) + "*(" + getTree(
                    tree.getRchild())
                    + ")+" + resovle(tree.getRchild()) + "*("
                    + getTree(tree.getLchild()) + "))";
        } else if (tree.getData().equals("^")) {
            String  a = new BigInteger(tree.getRchild().getData()).
                    subtract(BigInteger.ONE).toString();
            String ans = "((" + getTree(tree.getRchild()) + ")*" + getTree(
                    tree.getLchild()) + "^" + a + "*";
            return ans + resovle(tree.getLchild()) + ")";
        } else if (tree.getData().equals("s")) {
            return "cos(" + getTree(tree.getRchild()) + ")*"
                    + resovle(tree.getRchild());
        } else {
            return "(-sin(" + getTree(tree.getRchild()) + ")*"
                    + resovle(tree.getRchild()) + ")";
        }
    }

    public boolean isOp(String str) {
        if (str.equals("+") || str.equals("-") || str.equals("*")
                || str.equals("^")) {
            return true;
        }
        return false;
    }

    public boolean isX(String str) {
        Pattern string = Pattern.compile("\\d+|x|sin|cos");
        if (string.matcher(str).matches()) {
            return true;
        }
        return false;
    }

    public int getPriority(String s, String top) {
        if ((s.equals("+") || s.equals("-") || s.equals("*") || s.equals("^"))
                && (top.equals("s") || top.equals("c"))) {
            return -1;
        } else if ((s.equals("+") || s.equals("-") || s.equals("*"))
                && top.equals("^")) {
            return -1;
        } else if ((s.equals("+") || s.equals("-")) &&
                (top.equals("*") || top.equals("-"))) {
            return -1;
        } else {
            return 1;
        }
    }

    public String getTree(Node tree) {
        if (tree.getLchild() == null && tree.getRchild() == null) {
            return tree.getData();
        } else {
            if (tree.getData().equals("c") || tree.getData().equals("s")) {
                return getTree(tree.getLchild()) + "(" +
                        getTree(tree.getRchild()) + ")";
            } else {
                return "(" + getTree(tree.getLchild()) + tree.getData() +
                        getTree(tree.getRchild()) + ")";
            }
        }
    }
}
