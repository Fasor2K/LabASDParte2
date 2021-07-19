package Procedure;

public class RBTNode extends Node {

    public enum Color{
        RED, BLACK
    }

    public static final RBTNode NIL = new RBTNode();

    private Color color;

    public final static Color RED    = Color.RED;
    public final static Color BLACK  = Color.BLACK;

    public RBTNode() {
        super();
        this.color = BLACK;
    }

    public RBTNode(int key, String value) {
        super(key, value);
        this.left 	= RBTNode.NIL;
        this.right 	= RBTNode.NIL;
        this.parent	= RBTNode.NIL;
        this.color = RED;
    }

    public RBTNode getLeft() {
        return ((RBTNode) super.getLeft());
    }

    public RBTNode getRight() {
        return ((RBTNode) super.getRight());
    }

    public RBTNode getParent() {
        return ((RBTNode) super.getParent());
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
