package Procedure;

public class BST implements Trees {
    protected Node root;
    public BST() {
        root=Node.NIL;
    }


    /**
     * Inserisce un nodo nella posizione corretta del BST
     * @param key chiave del nodo da inserire
     * @param value contenuto testuale del nodo
     * @return booleano posto a true se l'operazione è avvenuta con successo
     */
    @Override
    public boolean insert(int key, String value) {
        Node x = root;
        Node y = Node.NIL;
        Node z = new Node(key, value);

        //Scendo nell'albero fino a trovare la posizione corretta per il nodo z da inserire
        while (!x.equals(Node.NIL)) {
            y = x;
            if (x.getKey() > z.getKey()) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }

        //Caso in cui non entro nel while (la radice è NIL, albero vuoto), il nodo viene inserito nella radice
        if (y.equals(Node.NIL)) {
            root = z;
        } else {
            //y sarà il genitore di z, il quale sarà figlio destro o sinistro in base al suo valore
            z.setParent(y);
            if (y.getKey() > z.getKey())
                y.setLeft(z);
            else
                y.setRight(z);
        }
        return true;
    }

    /**
     * Cerca il nodo con chiave key all'interno del BST
     * @param key chiave del nodo da cercare
     * @return il nodo con chiave k se esiste nel BST, altrimenti ritorna un nodo NIL
     */
    @Override
    public Node search(int key) {
        Node x = root;

        /*
        Partendo dalla radice mi sposto sul figlio destro o su quello sinistro in base al valore del nodo corrente
        e di quello della chiave ricercata, se trovo il nodo con chiave key lo ritorno, altrimenti arriverò in fondo
        all'albero (x.equals(Node.NIL)) e ritornerò un nodo NIL
         */
        while (!x.equals(Node.NIL)) {
            if (x.getKey() == key) {
                return x;
            } else if (x.getKey() > key) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }
        return Node.NIL;
    }

    @Override
    public String preOrderVisit(){
        return preOrderRec(root).trim();
    }

    private String preOrderRec(Node x){
        if(x.equals(Node.NIL)){
            return "NULL ";
        }
        else{
            return x.getKey()+":"+x.getValue()+" "+ preOrderRec(x.getLeft())+ preOrderRec(x.getRight());
        }
    }

    @Override
    public void reset() {
        this.root = Node.NIL;
    }
}
