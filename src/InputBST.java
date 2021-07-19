import Procedure.BST;
import Procedure.Node;

import java.util.Scanner;

public class InputBST {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        //ArrayList<String> lista=new ArrayList<String>();
        String line;
        String[] cmd;
        BST bst=new BST();

        while(!(line=scan.nextLine()).equals("exit") ){
            //lista.add(line);
            cmd=line.split(" ");
            if(cmd[0].equals("show")){
                System.out.println(bst.preOrderVisit());
            }
            else if(cmd[0].equals("insert")){
                bst.insert(Integer.parseInt(cmd[1].trim()),cmd[2].trim());
            }
            else if(cmd[0].equals("find")){
                Node x=bst.search(Integer.parseInt(cmd[1].trim()));
                if(!x.equals(Node.NIL)){
                    System.out.println(x.getValue());
                }
            }
        }
    }
}
