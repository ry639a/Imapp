/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imappserver;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
/**
 * On the other end of socket
 * Receives message from client. 
 * Sends it to destination 
 * 
 * Generates Session Keys and sends it to both the user
 * @author Ravali
 */
public class ImServer {
    
    //Starts the server socket
    public static void main(String[] args)
    {
        int portNumber = Integer.parseInt(args[0]);
        String message[] = new String[10];  
        ServerSocket serverSocket;
        int num_clients=0; 

        if(args.length<1){
            System.err.println("Error in input: Enter port number");
            System.exit(1);
        }
        
        for(int i=0;i<10;i++)
            message[i] = "";

        System.out.println("Starting the server...");
        System.out.println("<Port number>"+portNumber);
       
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Cheers! Server is up and running.It is now ready to take connections.");
            boolean listening =true;
            while(listening)
            {
                num_clients++;
                new ImServerThread(serverSocket,message).start(); 
                if(num_clients==10)
                   listening=false;
	    }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("Oops! Failure in starting server. Try again!");
            
        }
    }
}
