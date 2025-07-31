public class Message {
    public boolean isRead;
    public String sender;
    public String receiver;
    public String body;
    public String messId;
    public void add(String a,String b,String c,String id){
      sender=a;
      receiver=b;
      body=c;
      isRead=false;
      messId=id;
    }
}
