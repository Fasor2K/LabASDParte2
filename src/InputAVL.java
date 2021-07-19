import Procedure.AVL;
import Procedure.Node;

import java.util.Scanner;

public class InputAVL {
    public static void main(String args[]){
        Scanner scan = new Scanner(System.in);
        String line;
        String[] cmd;
        AVL avl=new AVL();

        while(!(line=scan.nextLine()).equals("exit") ){
            cmd=line.split(" ");
            if(cmd[0].equals("show")){
                System.out.println(avl.preOrderVisit());
            }
            else if(cmd[0].equals("insert")){
                avl.insert(Integer.parseInt(cmd[1].trim()),cmd[2].trim());
            }
            else if(cmd[0].equals("find")){
                Node x=avl.search(Integer.parseInt(cmd[1].trim()));
                if(!x.equals(Node.NIL)){
                    System.out.println(x.getValue());
                }
            }
        }
    }
}
