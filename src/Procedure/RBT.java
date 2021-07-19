package Procedure;

public class RBT extends Tree implements Trees {
    public RBT() {
        super();
        root = RBTNode.NIL;
    }


    /**
     * Esegue una rotazione a destra sul nodo passato come parametro
     * @param red nodo su cui effettuare la rotazione
     */
    private void rotateRight(RBTNode red) {
        RBTNode left = red.getLeft();
        //Il figlio destro del figlio sinistro di red diventa il figlio sinistro di red
        red.setLeft(left.getRight());
        //Se tale figlio non è nil, il suo genitore sarà red
        if (!left.getRight().equals(RBTNode.NIL)) {
            left.getRight().setParent(red);
        }
        /*
        Il figlio sinistro del nodo red deve salire poichè stiamo ruotando verso destra, il suo genitore sarà
        quindi il precedente genitore del nodo red
         */
        left.setParent(red.getParent());
        //Se il nodo red era la radice, allora la radice sarà il figlio sinistro di red
        if (red.parent.equals(RBTNode.NIL)) {
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
    private void rotateLeft(RBTNode red) {
        RBTNode right = red.getRight();
        red.setRight(right.getLeft());
        if (!right.getLeft().equals(RBTNode.NIL)) {
            right.getLeft().setParent(red);
        }
        right.setParent(red.getParent());
        if (red.parent.equals(RBTNode.NIL)) {
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
        insert(new RBTNode(key, value));
        return true;
    }

    private void insert(RBTNode z) {

        RBTNode y = RBTNode.NIL;
        RBTNode x = (RBTNode) root;

        //Scendo nell'albero fino a trovare la posizione corretta per il nodo z da inserire
        while (!x.equals(RBTNode.NIL)) {
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
        if (y.equals(RBTNode.NIL)) {
            root = z;
        //Decido se z sarà il figlio sinistro o destro di y in base alla sua chiave
        } else if (z.getKey() < y.getKey()) {
            y.setLeft(z);
        } else {
            y.setRight(z);
        }
        //Se z è la radice la coloro di nero ed esco dalla procedura
        if (z.getParent().equals(RBTNode.NIL)) {
            z.setColor(RBTNode.BLACK);
            return;
        }
        /*
        Se il nonno di z è nil posso terminare qui la procedura, altrimenti chiamo la procedura FixInsert
        per risolvere eventuali errori di posizionamento/colorazione del nuovo nodo appena inserito
         */
        if (z.getParent().getParent().equals(RBTNode.NIL)) {
            return;
        }
        FixInsert(z);

    }

    /**
     * Tramite rotazioni e ricolorazioni risolve eventuali problemi sul nodo z
     * @param z nodo z su cui risolvere eventuali problemi
     */
    private void FixInsert(RBTNode z) {
        RBTNode uncle;
        while (z.getParent().getColor()!=null && z.getParent().getColor().equals(RBTNode.RED)) {
            //Se il genitore di z è un figlio destro, allora lo zio di z sarà figlio sinistro del nonno di z (fratello del padre di z)
            if (z.getParent().equals(z.getParent().getParent().getRight())) {
                uncle = z.getParent().getParent().getLeft();

                /*
                Se lo zio di z è red lo coloro di nero, coloro poi il padre di z di nero ed il nonno di z di rosso.
                Mi sposto poi sul nonno di z e ripeto le operazioni
                 */
                if (uncle.getColor()!=null && uncle.getColor().equals(RBTNode.RED)) {
                    uncle.setColor(RBTNode.BLACK);
                    z.getParent().setColor(RBTNode.BLACK);
                    z.getParent().getParent().setColor(RBTNode.RED);
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
                    z.getParent().setColor(RBTNode.BLACK);
                    z.getParent().getParent().setColor(RBTNode.RED);
                    rotateLeft(z.getParent().getParent());
                }
            //else: genitore di z è un figlio sinistro, lo zio di z sarà figlio destro del nonno di z (fratello del padre di z)
            } else {
                //Le operazioni seguenti sono speculari a quelle viste in precedenza
                uncle = z.getParent().getParent().getRight();
                if (uncle.getColor()!=null && uncle.getColor().equals(RBTNode.RED)) {
                    uncle.setColor(RBTNode.BLACK);
                    z.getParent().setColor(RBTNode.BLACK);
                    z.getParent().getParent().setColor(RBTNode.RED);
                    z = z.getParent().getParent();

                } else {
                    if (z.equals(z.getParent().getRight())) {
                        z = z.getParent();
                        rotateLeft(z);
                    }
                    z.getParent().setColor(RBTNode.BLACK);
                    z.getParent().getParent().setColor(RBTNode.RED);
                    rotateRight(z.getParent().getParent());
                }

            }
            //Se arrivo alla radice dell'albero interrompo il ciclo
            if (z.equals(root)) {
                break;
            }
        }
        //La radice dell'RBT deve essere necessariamente black
        ((RBTNode) root).setColor(RBTNode.BLACK);
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
        this.root = RBTNode.NIL;
    }

    @Override
    public String preOrderVisit() {
        return preOrderRec((RBTNode) root);
    }

    private String preOrderRec(RBTNode x){
        String color;
        if(x.equals(Node.NIL)){
            return "NULL ";
        }
        else{
            if(x.getColor().equals(RBTNode.BLACK)){
                color="black";
            }
            else{
                color="red";
            }
            return x.getKey()+":"+x.getValue()+":"+color+" "+ preOrderRec(x.getLeft())+ preOrderRec(x.getRight());
        }
    }
}
