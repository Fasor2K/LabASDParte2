package Procedure;

public class RBTNode extends Node {

    public static final RBTNode NIL = new RBTNode();

    private byte color;

    public final static byte RED    = 0x0;
    public final static byte BLACK  = 0x1;

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

    public byte getColor() {
        return color;
    }

    public void setColor(byte color) {
        this.color = color;
    }
}
