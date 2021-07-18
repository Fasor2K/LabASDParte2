import Procedure.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        int numberOfTotalSamples = 100;
        int initialRepetition = 1000;
        int maxRepetitions = 1000000;
        int iterationsForPrecision = 50;
        double allowedError = 0.01;

        /*
        Numero di operazioni di ricerca/inserimento
         */
        int repetitionsCounter;

        BST bst = new BST();
        AVL avl = new AVL();
        RBT rbt = new RBT();
        ArrayList<Trees> trees = new ArrayList<>();
        trees.add(bst);
        trees.add(avl);
        trees.add(rbt);

        //Ripeto tutto 100 volte
        for (int i = 0; i < numberOfTotalSamples; i++) {
            //Determino il numero di ripetizioni delle operazioni di ricerca/inserimento
            double base = Math.exp((Math.log(maxRepetitions) - Math.log(initialRepetition)) / (numberOfTotalSamples - 1));
            repetitionsCounter = (int) ((double) initialRepetition * Math.pow(base, i));
            System.out.print(repetitionsCounter + " ");

            // Ripeto tutte le operazioni per ogni tipologia di albero
            for (Trees value : trees) {
                double deviation = 0.0;
                double currentTotalTime = 0.0;
                ArrayList<Double> tempMemory = new ArrayList<>();
                // Per ogni albero ripeto tutto iterationsForPrecision volte in modo da migliorare la precisione dei risultati
                for (int k = 0; k < iterationsForPrecision; k++) {
                    value.reset();
                    //Ogni volta che termino repetitionsCounter raccolgo il valore dei tempi
                    double currentTime = searchAndInsert(value, repetitionsCounter, allowedError);
                    currentTotalTime += currentTime;
                    tempMemory.add(currentTime);
                    System.gc();
                }
                //Tempo ammortizzato
                double ammortisedTime = currentTotalTime / iterationsForPrecision;
                System.out.print(ammortisedTime + " ");

                //Varianza
                for (Double time : tempMemory) {
                    deviation += Math.pow(time - ammortisedTime, 2);
                }
                deviation = (Math.sqrt(deviation / iterationsForPrecision));
                System.out.print(deviation + " ");

            }

            System.out.println();

        }

    }

    private static double getResolution() {
        double start = System.nanoTime();
        double end;
        do {
            end = System.nanoTime();
        } while (start == end);
        return end - start;
    }


    /*private static double getResolution() {
        long startT, endT;
        long[] value = new long[10000];

        for (int i = 0; i < 10000; i++) {
            startT = System.nanoTime();
            do {
                endT = System.nanoTime();
            } while (startT == endT);
            value[i] = endT - startT;
        }
        Arrays.sort(value);
        return value[value.length / 2];
    }*/


    /**
     * Rleva i tempi di esecuzione delle operazioni su un determinato albero
     * @param p oggetto di tipo Params contenente il vettore con i valori da cercare/inserire nell'albero ed un metodo
     *          per eseguire tutte le operazioni contenute nel vettore
     * @param risoluzione risoluzione della macchina
     * @param err errore massimo consentito
     * @return tempo di esecuzione diviso per il numero di iterazioni del ciclo do-while, tutto a sua volta diviso per il
     *         numero di inserimenti effettuati durante execute
     */
    private static double getTime(Params p, double risoluzione, double err) {

        long start, end;
        //c = iterazioni del ciclo do-while
        //c1 = numero di inserimenti effettuati durante execute
        int c = 0, c1;
        start = System.nanoTime();
        do {
            c1 = p.execute();
            end = System.nanoTime();
            c++;
        } while (end - start <= risoluzione * (1.0 / err) + 1.0);
        return ((end - start) / c) / c1;
    }

    /**
     * Esegue le operazioni di ricerca ed eventuale inserimento degli elementi non presenti sull'albero selezionato
     * @param tree albero su cui effettuare le operazioni
     * @param randomArraySize numero di valori casuali da cercare/inserire nell'albero
     * @param err errore massimo consentito
     * @return ritorna il risultato della funzione getTime
     */
    private static double searchAndInsert(Trees tree, int randomArraySize, double err) {
        double MinResolution = getResolution();
        RandomArray randomArray = new RandomArray();
        //Array di numeri randomici che verranno cercati ed eventualmente inseriti nell'albero
        int k[] = randomArray.newArray(randomArraySize);

        Params p = new Params() {
            public int k[];

            public void setK(int[] k) {
                this.k = k;
            }

            @Override
            public int execute() {
                //c = operazioni di inserimento (elementi non presenti)
                int c = 0;
                for (int i = 0; i < k.length; i++) {
                    if (tree.search(k[i]).equals(Node.NIL)) {
                        tree.insert(k[i], "test" + i);
                        c++;
                    }
                }
                return c;
            }

        };
        p.setK(k);
        return getTime(p, MinResolution, err);
    }
}


interface Params {
    int execute();

    void setK(int[] k);
}