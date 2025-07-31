
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{
    public Socket socket;
    public DataInputStream input;
    public DataOutputStream out;
    public static String[] c;

    public void run(){
    int port=Integer.parseInt(c[1]);
    try{
        socket=new Socket(c[0],port);//connect request

        out=new DataOutputStream(socket.getOutputStream());
    } catch (UnknownHostException e) {
        throw new RuntimeException(e);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    String command= String.join(" ",c);
    try{
        out.writeUTF(command);//Request for data
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
        try {
            input=new DataInputStream(new BufferedInputStream(socket.getInputStream()));//gets the reply
            System.out.println(input.readUTF());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {//disconnect
        out.close();
        input.close();
        socket.close();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
    public static void main(String[] args){
            c=args;//user's input
            Client thread=new Client();
            thread.start();
    }

}
