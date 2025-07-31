import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Character.isAlphabetic;
public class Server {
   public static Socket socket;
  public static ServerSocket server;
   public static DataInputStream input;
    public static List<Account> AccountBox=new ArrayList<>();
    public static DataOutputStream out;
    public static String output;

    public Server(int port){
    }
    public static int addAcc(String username)// 1
    {
        char us[]=username.toCharArray();
        for(char c:us)
        {
            if(!isAlphabetic(c)){
                return -1;
            }
        }
        for (int i = 0; i < AccountBox.size(); i++) {
            if (AccountBox.get(i).username.equals(username)) {
                return 0;
            }
        }
        Account account = new Account();
        int authToken = account.createAcc(username);
        AccountBox.add(account);
        return authToken;
    }

    public static String showAcc(){// 2
        int n=AccountBox.size();
        String listOfAcc[] = new String[n];
        for (int i = 1; i <= n; i++)
        {
            listOfAcc[i-1]=(i+". "+AccountBox.get(i-1).username);
        }
        return String.join("\n",listOfAcc);
    }

    public static String showInbox(String username){// 4
        String listOfInbox[] = null;
        int n=0;
        for (int i = 0; i < AccountBox.size(); i++) {
            if (AccountBox.get(i).username.equals(username)) {
                n=AccountBox.get(i).messageBox.size();
                listOfInbox= new String[n];
                for (int j=0;j<n;j++){
                    String read="*";
                    if(AccountBox.get(i).messageBox.get(j).isRead){
                        read="";
                    }
                    listOfInbox[j]=(AccountBox.get(i).messageBox.get(j).messId+". from: "+AccountBox.get(i).messageBox.get(j).sender+read);
                }
            }
        }
        if(n!=0) {
            return String.join("\n", listOfInbox);
        }
        else{
            return "";
        }
    }
    public static String checkAuthToken(String aToken){
        for (int i = 0; i < AccountBox.size(); i++) {
            if (String.valueOf(AccountBox.get(i).authToken).equals(aToken)) {
                return AccountBox.get(i).username;
            }
        }
        return "0";
    }

   public static int sendMessage(String sender, String recipient, String mess){// 3
       for (int i = 0; i < AccountBox.size(); i++) {
           if (AccountBox.get(i).username.equals(recipient)) {
               AccountBox.get(i).addMess(sender,recipient,mess);
               return 1;
           }
       }
       return 0;
   }
   public static String readMess(String user,String messId){ //5
       for (int i = 0; i < AccountBox.size(); i++) {
           if (AccountBox.get(i).username.equals(user)) {
               int n=AccountBox.get(i).messageBox.size();
               for (int j=0;j<n;j++){
                   if(AccountBox.get(i).messageBox.get(j).messId.equals(messId)){
                       AccountBox.get(i).messageBox.get(j).isRead=true;
                       return "("+AccountBox.get(i).messageBox.get(j).sender+") "+AccountBox.get(i).messageBox.get(j).body;
                   }
               }

           }
       }
        return "Message ID does not exist";
   }

   public static String deleteMessage(String username,String id){ //6

       for (int i = 0; i < AccountBox.size(); i++)
       {
           if (AccountBox.get(i).username.equals(username))
           {
               int n=AccountBox.get(i).messageBox.size();
               for (int j=0;j<n;j++)
               {
                   if (AccountBox.get(i).messageBox.get(j).messId.equals(id))
                   {
                       AccountBox.get(i).messageBox.remove(j);
                       return "OK";
                   }
               }
           }
       }
        return "Message does not exist";
   }

    public static void main(String[] args) throws IOException {
        int port= Integer.parseInt(args[0]);
        server=new ServerSocket(port);
        while(true){
        try{
            socket=server.accept();//Accept response
            input=new DataInputStream(new BufferedInputStream(socket.getInputStream()));//gets the data
            try {
                String mess=input.readUTF();
                String comm[]=mess.split(" ",6);
                if (comm[2].equals("1"))
                {
                    int authToken=addAcc(comm[3]);
                    if (authToken == 0) {
                        output= "Sorry, the user already exists";
                    }
                    else if (authToken==-1)
                    {
                        output="Invalid Username";
                    }  else
                    {
                        output= String.valueOf(authToken);
                    }
                }
                else if (Integer.parseInt(comm[2])<=6)
                {
                    String user=checkAuthToken(comm[3]);//find Auth Token's username
                   if (!user.equals("0"))
                   {
                       if (comm[2].equals("2"))
                       {
                           output = showAcc();
                       }
                       else if (comm[2].equals("3")) {
                           if (sendMessage(user,comm[4],comm[5])==1){
                               output="OK";
                           }
                           else{
                               output="User does not exist";
                           }
                       }
                       else if (comm[2].equals("4"))
                       {
                          output=showInbox(user);
                       }
                       else if (comm[2].equals("5"))
                       {
                            output=readMess(user,comm[4]);
                       } else if (comm[2].equals("6"))
                       {
                            output=deleteMessage(user,comm[4]);
                       }
                   }
                   else {
                       output = "Invalid Auth Token";
                   }
                }
                else
                {
                    output="Invalid FN_ID";
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
            out=new DataOutputStream(socket.getOutputStream());
            out.writeUTF(output);//Reply
        }
    }
}
