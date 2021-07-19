import Procedure.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TimesRecording {

    private static String textToFile;
    private static final int internalIterations = 50, minRep = 1000,maxRep = 1000000,iterations = 100;
    private static final double E = 0.01;

    public static void main(String args[]){
        timesOnFile();
    }

    public static void timesOnFile(){
        double base;
        int operations;

        BST bst = new BST();
        AVL avl = new AVL();
        RBT rbt = new RBT();

        String path="data.txt";
        path=writeFile(path,"",true);

        for(int i=0;i<iterations;i++){
            textToFile="";
            //Determino il numero di ripetizioni delle operazioni di ricerca/inserimento
            base = Math.exp((Math.log(maxRep) - Math.log(minRep)) / (iterations - 1));
            operations = (int) ((double) minRep * Math.pow(base, i));
            textToFile+=operations+" ";

            timesRecorder(bst,operations);
            timesRecorder(avl,operations);
            timesRecorder(rbt,operations);

            path=writeFile(path,textToFile+"\n",false);
        }
    }

    private static void timesRecorder(Trees tree,int op){
        double deviation = 0.0,totalTime = 0.0,resolution;
        double start, end,actualTime;
        //c = iterazioni del ciclo do-while
        //c1 = numero di inserimenti effettuati durante execute
        int c = 0, c1=0;
        int[] nums;
        ArrayList<Double> tempMemory = new ArrayList<>();
        RandomArray randomArray=new RandomArray();
        for (int k = 0; k < internalIterations; k++) {
            nums=randomArray.newArray(op);
            resolution=getResolution();
            tree.reset();

            start=System.nanoTime();

            do {
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
            deviation += Math.pow(time - ammortisedTime, 2);
        }
        deviation = (Math.sqrt(deviation / internalIterations));
        textToFile+=deviation + " ";
    }


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
