import java.util.ArrayList;
import java.util.List;
public class Account {
    public String username;
    public int authToken;
   public List<Message> messageBox=new ArrayList<>();
   public static int authTok=1000;
   public static int id=1;
   public int createAcc(String usernam)
   {
       username=usernam;
       authToken=authTok++;
       return authToken;
   }
   public void addMess(String sender,String recipient,String mess){
       Message message=new Message();
       message.add(sender,recipient,mess, String.valueOf(id));
       messageBox.add(message);
       id++;
   }
}
