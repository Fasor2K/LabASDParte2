package Procedure;

public class AVL extends Tree implements Trees {
    public AVL() {
        root = (AvlNode) AvlNode.NIL;
    }

    /**
     * Ritorna la differenza di altezza tra il ramo sinistro ed il ramo destro
     * di un nodo (numero di nodi sbilanciati)
     * @param avl nodo in questione
     * @return diffenza di altezza tra nodo destro e sinistro
     */
    private int unBalancedNodes(AvlNode avl) {
        if (!avl.equals(AvlNode.NIL)) {
            return getHeight(avl.getLeft()) - getHeight(avl.getRight());
        }
        return 0;
    }


    /**
     * Ritorna l'altezza di un nodo passato come parametro
     * @param avl nodo di cui si vuole sapere l'altezza
     * @return altezza del nodo avl
     */
    private int getHeight(AvlNode avl) {
        if (avl == null || avl.equals(AvlNode.NIL)) {
            return 0;
        }
        return avl.getHeight();
    }

    /**
     * Esegue una rotazione a destra rispetto al nodo avlR
     * @param avlR nodo perno su cui effettuare la rotazione
     * @return
     */
    private AvlNode rotateRight(AvlNode avlR) {
        AvlNode avlL = avlR.getLeft();
        AvlNode b = avlL.getRight();

        //Il figlio sinistro di avlR sale, avlR sarà invece il suo figlio destro
        avlL.setRight(avlR);

        //Il figlio destro di avlL diventerà figlio sinistro di avlR
        avlR.setLeft(b);

        /*
        Determino l'altezza dei nodi in base all'altezza massima tra quella del ramo sinistro e quella
        del ramo destro aumentata di 1
         */
        avlR.setHeight(Math.max(getHeight(avlR.getLeft()), getHeight(avlR.getRight())) + 1);
        avlL.setHeight(Math.max(getHeight(avlL.getLeft()), getHeight(avlL.getRight())) + 1);

        return avlL;
    }


    private AvlNode rotateLeft(AvlNode avlL) {
        AvlNode avlR = avlL.getRight();
        AvlNode b = avlR.getLeft();

        avlR.setLeft(avlL);
        avlL.setRight(b);

        avlL.setHeight(Math.max(getHeight(avlL.getLeft()), getHeight(avlL.getRight())) + 1);
        avlR.setHeight(Math.max(getHeight(avlR.getLeft()), getHeight(avlR.getRight())) + 1);

        return avlR;
    }


    /**
     * Procedura ricorsiva che inserisce il nodo di chiave key con contenuto value nella posizione corretta
     * @param avl nodo padre da cui parto per l'inserimento, cambierà nelle varie chiamate ricorsive finchè non verrà trovato quello corretto
     * @param key chiave del nodo da inserie
     * @param value contenuto del nodo da inserire
     * @return ritorna il nodo padre di quella specifica chiamata ricorsiva dopo che le operazioni di inserimento sono state completate
     */
    private Node insert(AvlNode avl, int key, String value) {

        //Se il nodo padre è nil allora creo un nuovo nodo con le caratteristiche desiderate e lo ritorno
        if (avl.equals(AvlNode.NIL)) {
            return (new AvlNode(key, value, 1));
        }
        /*
        Altrimenti inserisco ricorsivamente (a sinistra o a destra a seconda della chiave key) il nodo.
        Il sottoalbero con il nodo correttamente inserito sarà il risultato della chiamata ricorsiva della
        funzione insert
         */
        if (key < avl.getKey()) {
            avl.setLeft(insert(avl.getLeft(), key, value));
        } else {
            avl.setRight(insert(avl.getRight(), key, value));
        }

        //Aggiorno l'altezza dell'albero dopo l'inserimento
        avl.setHeight(1 + Math.max(getHeight(avl.getLeft()), getHeight(avl.getRight())));

        /*
        Se ci sono nodi sbilanciati, eseguo rotazioni per sistemare l'albero a seconda dei casi, ritorno
        poi la radice dell'albero corretto
         */
        int balance = unBalancedNodes(avl);

        if (balance > 1 && key < avl.getLeft().getKey()) {
            return rotateRight(avl);
        }

        if (balance < -1 && key > avl.getRight().getKey()) {
            return rotateLeft(avl);
        }

        if (balance > 1 && key > avl.getLeft().getKey()) {
            avl.setLeft(rotateLeft(avl.getLeft()));
            return rotateRight(avl);
        }

        if (balance < -1 && key < avl.getRight().getKey()) {
            avl.setRight(rotateRight(avl.getRight()));
            return rotateLeft(avl);
        }

        return avl;
    }


    /**
     * Procedura che invoca la procedura privata ricorsiva insert passandogli la radice dell'albero.
     * La procedura privata andrà poi ad effettuare l'inserimento
     * @param key chiave del nodo da inserie
     * @param value contenuto del nodo da inserire
     * @return booleano posto a true se l'operazione avviene con successo
     */
    @Override
    public boolean insert(int key, String value) {
        this.root = insert((AvlNode) this.root, key, value);
        return true;
    }


    /**
     * Cerca il nodo con chiave che all'interno dell'albero
     * @param key chiave dell'albero da trovare
     * @return ritorna il nodo con chiave key se presente nell'albero, altrimenti ritorna un nodo nil
     */
    @Override
    public AvlNode search(int key) {
        AvlNode x = (AvlNode) root;

        while (!x.equals(AvlNode.NIL)) {
            if (x.getKey() == key) {
                return x;
            } else if (x.getKey() > key) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }

        return AvlNode.NIL;
    }

    @Override
    public void reset() {
        this.root = (AvlNode) AvlNode.NIL;
    }

    @Override
    public String preOrderVisit() {
        return preOrderRec((AvlNode) root);
    }

    private String preOrderRec(AvlNode x){
        if(x.equals(Node.NIL)){
            return "NULL ";
        }
        else{
            return x.getKey()+":"+x.getValue()+":"+x.getHeight()+" "+preOrderRec(x.getLeft())+preOrderRec(x.getRight());
        }
    }
}
