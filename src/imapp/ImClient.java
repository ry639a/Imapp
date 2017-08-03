/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imapp;

import java.net.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import org.apache.commons.codec.binary.Base64; 
/**
 * Client establishes the connection with server. 
 * Processes the input Encryption or Decryption based on whether its on sending(send = true) or receiving end(send=false).
 * @author Ravali
 */
public class ImClient {
    /* Creates a socket and ServerConnection code is written here
    */
    public static void main(String[] args)
    {
        
        if(args.length!=6){
        System.err.println("Error in input: <HostName> <Port Number>");
        System.exit(1);
        }

        
        Socket clientSocket=null;
        String HostName = args[0];
        int portNumber = Integer.parseInt(args[1]); 
        int fromClient = Integer.parseInt(args[2]);
        String passsw = checkPassword(args[3]);
        String input = args[4];
        int toClient = Integer.parseInt(args[5]);
        String output = "";
        String[] passwords = new String[5];
        passwords[0] = checkPassword("1111");
        passwords[1] = checkPassword("2222");
        passwords[2] = checkPassword("3333");
        passwords[3] = checkPassword("4444");
        passwords[4] = checkPassword("5555");
        
        if(passwords[fromClient-1].equals(passsw))
        {
        try
        {
            clientSocket = new Socket(HostName, portNumber);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Oops! Failure in connecting clients");
        }
        
        String cipherInput = encrypt(input);
        String sentmesage = "Client"+fromClient + ":" + cipherInput;
        send(clientSocket,sentmesage,fromClient,toClient); 
        read(clientSocket,input,fromClient,toClient);
        System.out.println("Sent:");
        System.out.println("______");
        System.out.println("Plain:"+input);
        System.out.println(sentmesage);
        }
        else 
        {
            System.out.println("Authentication failed");
        }
    }    
    
    public static void send(Socket clientSocket, String input,int fromClient, int toClient)
    {
        /* Encrypts the input
        * Appends fromClient to message
        * Sends the message to server
        */

        try
        {
            //Output to send information to server socket
            PrintWriter pwout = new PrintWriter(clientSocket.getOutputStream(), true);
            pwout.println(toClient);
            pwout.println(input);        
        }
        catch(IOException e)
        {
             e.printStackTrace();
        }
    }
    
    public static void read(Socket clientSocket,String input,int fromClient,int toClient)
    {
        /* Reads recived messages from buffereReader from server
          Decrypts the received message
          prints it
        */
        try
        {
            //System.out.println("REached this");
            BufferedReader bin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //InputStream to recieve response from server
            
            //String ptext = decrypt(bin.readLine());
            System.out.println("Received:");
            System.out.println("__________");
            System.out.println("cipher:"+bin.readLine());
            //System.out.println("Plain:"+"Hey");
                //System.out.println("REached this tootootoo");
        }
        catch(IOException e)
        {
             e.printStackTrace();
        }
    }
    
    //Encrypts the text message
    public static String checkPassword(String input)
    {
        String output = input;
        try{
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(input.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
        }
        output = sb.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return output;
    }
    
    public static String encrypt(String input)
    {
        Cipher cipher; 
        String code = "1234567812345678";
        SecretKeySpec key = new SecretKeySpec(code.getBytes(), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(code.getBytes()); 
        String output=input; 
       
        
        try{    
        //Whatever you want to encrypt/decrypt
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // You can use ENCRYPT_MODE (ENCRYPTunderscoreMODE)  or DECRYPT_MODE (DECRYPT underscore MODE) 
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec); 

        // encrypt data 
         byte[] ecrypted = cipher.doFinal(input.getBytes("utf-8"));

         // encode data using standard encoder
         output =  Base64.encodeBase64String(ecrypted);  
        }
        catch(Exception e)
        { e.printStackTrace();}

        return output;
    }
    //decrypts the text message
    public static String decrypt(String input)
    {
        String output = input;
        
        String code = "1234567812345678";
        SecretKeySpec key = new SecretKeySpec(code.getBytes(), "AES");
        byte bytekey[];
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(code.getBytes());
        try{
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            
            //You can use ENCRYPT_MODE or DECRYPT_MODE 
            cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);  
            //decode data using standard decoder
            byte[] st =  Base64.decodeBase64(input);  
           // Decrypt the data 
           //System.out.println(st.length);
           System.out.println("Hey");
            byte[] decrypted = cipher.doFinal(st);
            output = decrypted.toString();
            //System.out.println("Original string: " + new String(input));
        }
        catch(Exception e)
        {
           e.printStackTrace(); 
        }
        return output;
    }  

}
