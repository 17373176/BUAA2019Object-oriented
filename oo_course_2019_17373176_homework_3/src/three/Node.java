package three;

public class Node {
    private String data;
    private Node lchild;
    private Node rchild;
    //private String ans;

    public Node() {
    }

    public Node(String data) {
        this.data = data;
        this.lchild = null;
        this.rchild = null;
    }

    public Node(String data, Node lchild, Node rchild) {
        super();
        this.data = data;
        this.lchild = lchild;
        this.rchild = rchild;
    }

    public String getData() {
        return data;
    }

    public Node getLchild() {
        return lchild;
    }

    public Node getRchild() {
        return rchild;
    }

}
