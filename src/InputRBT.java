import Procedure.RBT;
import Procedure.Node;

import java.util.Scanner;

public class InputRBT {
    public static void main(String args[]){
        Scanner scan = new Scanner(System.in);
        String line;
        String[] cmd;
        RBT rbt=new RBT();

        while(!(line=scan.nextLine()).equals("exit") ){
            cmd=line.split(" ");
            if(cmd[0].equals("show")){
                System.out.println(rbt.preOrderVisit());
            }
            else if(cmd[0].equals("insert")){
                rbt.insert(Integer.parseInt(cmd[1].trim()),cmd[2].trim());
            }
            else if(cmd[0].equals("find")){
                Node x=rbt.search(Integer.parseInt(cmd[1].trim()));
                if(!x.equals(Node.NIL)){
                    System.out.println(x.getValue());
                }
            }
        }
    }
}
