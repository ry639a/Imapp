/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imappserver;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import javax.crypto.KeyGenerator;
/**
 *
 * @author Ravali
 */
public class ImServerThread extends Thread{
    private Socket serviceSocket = null;
    private String name = "";
    private Thread t;
    String text = "";
    int clientId = 1;
    String message[] = new String[10];
    
    public ImServerThread(ServerSocket serverSocket,String[] message) throws IOException {
        
        System.out.println("Listening..");
        this.serviceSocket = serverSocket.accept();
        this.message = message;
    }
     
    public void run() {
        try 
        {
            //Reads input from client
            
            BufferedReader bin = new BufferedReader(new InputStreamReader(serviceSocket.getInputStream()));
            int toClient = Integer.parseInt(bin.readLine());
            String text = bin.readLine();
            System.out.println("Message:"+text);
            message[toClient]=text;
            this.name = text.split(":")[0];
            clientId = Integer.parseInt(name.substring(6));
            System.out.println("Connected:"+name);
            System.out.println("clientID:"+clientId);            

            //Writes response to client
            PrintWriter pwout = new PrintWriter(serviceSocket.getOutputStream(), true);
            if(!(message[clientId].isEmpty()))
            {
                System.out.println(message[clientId]);
                pwout.println(message[clientId]);
            }
            else
            {
                
              pwout.println("No Messages");   
            }
            
            if(text.equals("Bye"))
            {
                System.out.println(text);     
                serviceSocket.close();
                
                //pwClient.remove(clientId);
               // brClient.remove(clientId);   
            }            
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }     
    }
    public int UpdateSessionKey()
    {
        int key = 0;
        /*KeyGenerator kg = new KeyGenerator();
        .init(54);*/
        return key;
    }
}
