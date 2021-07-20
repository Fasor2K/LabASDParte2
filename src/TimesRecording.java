import Procedure.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TimesRecording {

    private static String textToFile;
    private static final int internalIterations = 50, minRep = 1000,maxRep = 1000000, totalIterations = 100;
    private static final double E = 0.01;

    public static void main(String args[]){
        timesOnFile();
    }

    /**
     * Chiama la procedura privata timesRecorder per ogni albero e scrive su file i risultati, ripete tutto
     * totalIterations volte
     */
    public static void timesOnFile(){
        double base;
        int operations;

        BST bst = new BST();
        AVL avl = new AVL();
        RBT rbt = new RBT();

        String path="data.txt";
        /*
        Eseguo una scrittura vuota sul file in modo tale che la funzione mi restituisca l'effettivo nome del file,
        in caso data.txt sia già presente nel disco
         */
        path=writeFile(path,"",true);

        for(int i = 0; i< totalIterations; i++){
            textToFile="";
            //Determino il numero di ripetizioni delle operazioni di ricerca/inserimento
            base = Math.exp((Math.log(maxRep) - Math.log(minRep)) / (totalIterations - 1));
            operations = (int) ((double) minRep * Math.pow(base, i));
            textToFile+=operations+" ";

            timesRecorder(bst,operations);
            timesRecorder(avl,operations);
            timesRecorder(rbt,operations);

            path=writeFile(path,textToFile+"\n",false);
        }
    }

    /**
     * Esegue le rilevazioni dei tempi per un albero in particolare. Il numero di interi da cercare/inserire viene passato
     * come parametro alla funzione. Dato tale valore, la funzione genera ad ogni iterazione un vettore di interi randomici
     * che sarà oggetto delle operazioni che svolgeremo sull'albero. Per ogni vettore le misurazioni vengono ripetute finchè
     * non si raggiunge il tempo minimo misurabile dalla macchina. Una volta terminate le misurazioni il tempo ammortizzato
     * e la deviazione standard vengono inseriti nella stringa textToFile che verrà poi scritta su file
     * @param tree albero su cui effettuare le operazioni
     * @param op dimensione nel vettore randomico, corrisponde al numero di interi che andremo a cercare/inserire nell'albero
     */
    private static void timesRecorder(Trees tree,int op){
        double standardDeviation = 0.0,totalTime = 0.0,resolution;
        double start, end,actualTime;
        //c = iterazioni del ciclo do-while
        //c1 = numero di inserimenti effettuati durante execute
        int c = 0, c1=0;
        int[] nums;
        //ArrayList dove salverò il tempo di ogni iterazione per calcolare la deviazione standard
        ArrayList<Double> tempMemory = new ArrayList<>();
        RandomArray randomArray=new RandomArray();
        for (int k = 0; k < internalIterations; k++) {
            nums=randomArray.newArray(op);
            resolution=getResolution();

            start=System.nanoTime();

            do {
                tree.reset();
                for (int i = 0; i < nums.length; i++) {
                    if (tree.search(nums[i]).equals(Node.NIL)) {
                        tree.insert(nums[i], "test" + i);
                        c1++;
                    }
                }
                end = System.nanoTime();
                c++;
            } while (end - start <= resolution * (1.0 / E) + 1.0);

            actualTime=((end-start)/c)/c1;
            totalTime+=actualTime;
            tempMemory.add(actualTime);

            System.gc();
        }
        //Tempo ammortizzato
        double ammortisedTime = totalTime / internalIterations;
        textToFile+=ammortisedTime + " ";

        //Varianza
        for (Double time : tempMemory) {
            standardDeviation += Math.pow(time - ammortisedTime, 2);
        }
        standardDeviation = (Math.sqrt(standardDeviation / internalIterations));
        textToFile+=standardDeviation + " ";
    }


    /**
     * Scrive su file una stringa passata come parametro, è in grado di creare il file se esplicitamente richiesto.
     * Se il file è gia esistente, modifica il nome del file finchè non ne trova uno non ancora utilizzato
     * @param path Percorso del file desiderato
     * @param text Testo da scrivere nel file
     * @param create Booleano che mi consente di decidere se il file va creato oppure se devo scrivere in coda ad un file già esistente
     * @return Eventuale nuovo percorso del file se quello desiderato (parametro path) è già in uso
     */
    private static String writeFile(String path,String text,boolean create){
        File f=new File(path);
        FileWriter fw;
        int k=1,l;
        final String oldpath=path; //Mi salvo il percorso originario del file

        if(create) { //Se create==true allora voglio creare il file
            while (f.exists()) { //Se il file esiste già cambia il nome del file aggiungendoci un'appendice "_k" finchè non trova un nome libero
                path=oldpath;
                if (path.contains(".")) { //Se il file ha un'estensione divido l'estensione dal resto della stringa
                    String[] arr = path.split("\\.");
                    l = arr.length;
                    arr[l - 2] += "_" + k + "."; //Aggiungo alla parte senza estensione l'appendice _k
                    path = arr[l - 2] + arr[l - 1]; //Aggiungo nuovamente l'estensione al nuovo percorso del file
                } else { //Se il file non ha estensione aggiungo semplicemente l'appendice alla fine del percorso
                    path += "_" + k;
                }
                k++;
                f = new File(path);
                /**
                 * Istanzio nuovamente l'oggetto f di tipo File con il nuovo percorso, alla successiva iterazione controllerò
                 * Se anche il nuovo percorso è già occupato, in tal caso, riprendo il percorso originario del file e ripeto la
                 * procedura con k incrementato finchè non trovo una posizione libera
                 */
            }

            try {
                f.createNewFile();
                fw = new FileWriter(f);
                fw.write(text);
                fw.close();
            } catch (IOException e) {
                System.out.println("ERRORE NELLA CREAZIONE DEL FILE");
                e.printStackTrace();
            }
        }
        else{
            f=new File(path);
            try {
                fw=new FileWriter(path,true);
                fw.write(text);
                fw.close();
            } catch (IOException e) {
                System.out.println("ERRORE NELLA SCRITTURA DEL FILE");
                e.printStackTrace();
            }
        }
        return path;
    }

    private static double getResolution() {
        double start = System.nanoTime();
        double end;
        do {
            end = System.nanoTime();
        } while (start == end);
        return end - start;
    }
}
