package Procedure;

public class RBT extends Tree implements Trees {
    public RBT() {
        super();
        root = RedBlackTreeNode.NIL;
    }


    /**
     * Esegue una rotazione a destra sul nodo passato come parametro
     * @param red nodo su cui effettuare la rotazione
     */
    private void rotateRight(RedBlackTreeNode red) {
        RedBlackTreeNode left = red.getLeft();
        //Il figlio destro del figlio sinistro di red diventa il figlio sinistro di red
        red.setLeft(left.getRight());
        //Se tale figlio non è nil, il suo genitore sarà red
        if (!left.getRight().equals(RedBlackTreeNode.NIL)) {
            left.getRight().setParent(red);
        }
        /*
        Il figlio sinistro del nodo red deve salire poichè stiamo ruotando verso destra, il suo genitore sarà
        quindi il precedente genitore del nodo red
         */
        left.setParent(red.getParent());
        //Se il nodo red era la radice, allora la radice sarà il figlio sinistro di red
        if (red.parent.equals(RedBlackTreeNode.NIL)) {
            this.root = left;
        /*
        Visto che il nodo left prenderà il posto di red se red era figlio sinistro allora il figlio sinistro
        del suo genitore diventerà left, altrimenti left diventerà il figlio destro del genitore di red
         */
        } else if (red.equals(red.getParent().getRight())) {
            red.getParent().setRight(left);
        } else {
            red.getParent().setLeft(left);
        }
        //Per concludere red scenderà a destra, diventando il figlio destro di left
        left.setRight(red);
        red.setParent(left);
    }

    /**
     * Esegue una rotazione a sinistra sul nodo passato come parametro
     * @param red nodo su cui effettuare la rotazione
     */
    private void rotateLeft(RedBlackTreeNode red) {
        RedBlackTreeNode right = red.getRight();
        red.setRight(right.getLeft());
        if (!right.getLeft().equals(RedBlackTreeNode.NIL)) {
            right.getLeft().setParent(red);
        }
        right.setParent(red.getParent());
        if (red.parent.equals(RedBlackTreeNode.NIL)) {
            this.root = right;
        } else if (red.equals(red.getParent().getLeft())) {
            red.getParent().setLeft(right);
        } else {
            red.getParent().setRight(right);
        }
        right.setLeft(red);
        red.setParent(right);
    }


    /**
     * Inserisce un nodo nell'albero corrente prendendo come input la chiave ed il contenuto testuale del nodo.
     * Viene poi chiamata una procedura privata a cui viene passato il nodo già istanziato
     * @param key chiave del nodo da inserire
     * @param value contenuto testuale del nodo da inserire
     * @return un booleano pari a true se l'operazione è avvenuta con successo
     */
    @Override
    public boolean insert(int key, String value) {
        insert(new RedBlackTreeNode(key, value));
        return true;
    }

    private void insert(RedBlackTreeNode z) {

        RedBlackTreeNode y = RedBlackTreeNode.NIL;
        RedBlackTreeNode x = (RedBlackTreeNode) root;

        //Scendo nell'albero fino a trovare la posizione corretta per il nodo z da inserire
        while (!x.equals(RedBlackTreeNode.NIL)) {
            y = x;
            if (z.getKey() < x.getKey()) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }
        //y arriverà quindi ad essere il genitore di z
        z.setParent(y);
        //se y è nil (albero vuoto) allora z diventerà la radice dell'albero
        if (y.equals(RedBlackTreeNode.NIL)) {
            root = z;
        //Decido se z sarà il figlio sinistro o destro di y in base alla sua chiave
        } else if (z.getKey() < y.getKey()) {
            y.setLeft(z);
        } else {
            y.setRight(z);
        }
        //Se z è la radice la coloro di nero ed esco dalla procedura
        if (z.getParent().equals(RedBlackTreeNode.NIL)) {
            z.setColor(RedBlackTreeNode.BLACK);
            return;
        }
        /*
        Se il nonno di z è nil posso terminare qui la procedura, altrimenti chiamo la procedura FixInsert
        per risolvere eventuali errori di posizionamento/colorazione del nuovo nodo appena inserito
         */
        if (z.getParent().getParent().equals(RedBlackTreeNode.NIL)) {
            return;
        }
        FixInsert(z);

    }

    /**
     * Tramite rotazioni e ricolorazioni risolve eventuali problemi sul nodo z
     * @param z nodo z su cui risolvere eventuali problemi
     */
    private void FixInsert(RedBlackTreeNode z) {
        RedBlackTreeNode uncle;
        while (z.getParent().getColor() == RedBlackTreeNode.RED) {
            //Se il genitore di z è un figlio destro, allora lo zio di z sarà figlio sinistro del nonno di z (fratello del padre di z)
            if (z.getParent().equals(z.getParent().getParent().getRight())) {
                uncle = z.getParent().getParent().getLeft();

                /*
                Se lo zio di z è red lo coloro di nero, coloro poi il padre di z di nero ed il nonno di z di rosso.
                Mi sposto poi sul nonno di z e ripeto le operazioni
                 */
                if (uncle.getColor() == RedBlackTreeNode.RED) {
                    uncle.setColor(RedBlackTreeNode.BLACK);
                    z.getParent().setColor(RedBlackTreeNode.BLACK);
                    z.getParent().getParent().setColor(RedBlackTreeNode.RED);
                    z = z.getParent().getParent();
                //else: zio di z è black
                } else {
                    //Se invece z è un figlio sinistro eseguo una rotazione verso destra su suo padre
                    if (z.equals(z.getParent().getLeft())) {
                        z = z.getParent();
                        rotateRight(z);
                    }
                    /*
                    Coloro poi il padre di z di nero, il nonno di z di rosso ed infine eseguo una rotazione
                    verso sinistra sul nonno di z
                     */
                    z.getParent().setColor(RedBlackTreeNode.BLACK);
                    z.getParent().getParent().setColor(RedBlackTreeNode.RED);
                    rotateLeft(z.getParent().getParent());
                }
            //else: genitore di z è un figlio sinistro, lo zio di z sarà figlio destro del nonno di z (fratello del padre di z)
            } else {
                //Le operazioni seguenti sono speculari a quelle viste in precedenza
                uncle = z.getParent().getParent().getRight();
                if (uncle.getColor() == RedBlackTreeNode.RED) {
                    uncle.setColor(RedBlackTreeNode.BLACK);
                    z.getParent().setColor(RedBlackTreeNode.BLACK);
                    z.getParent().getParent().setColor(RedBlackTreeNode.RED);
                    z = z.getParent().getParent();

                } else {
                    if (z.equals(z.getParent().getRight())) {
                        z = z.getParent();
                        rotateLeft(z);
                    }
                    z.getParent().setColor(RedBlackTreeNode.BLACK);
                    z.getParent().getParent().setColor(RedBlackTreeNode.RED);
                    rotateRight(z.getParent().getParent());
                }

            }
            //Se arrivo alla radice dell'albero interrompo il ciclo
            if (z.equals(root)) {
                break;
            }
        }
        //La radice dell'RBT deve essere necessariamente black
        ((RedBlackTreeNode) root).setColor(RedBlackTreeNode.BLACK);
    }


    /**
     * Cerca il nodo con chiave key all'interno dell'RBT
     * @param key chiave del nodo da cercare
     * @return ritorna il nuovo con chiave key se presente nell'RBT, altrimenti, ritorna un nodo nil
     */
    @Override
    public Node search(int key) {
        Node x = root;

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
    public void reset() {
        this.root = RedBlackTreeNode.NIL;
    }
}
