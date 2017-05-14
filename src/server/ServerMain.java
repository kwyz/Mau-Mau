package server;

import java.io.*;
import java.net.*;

public class ServerMain extends Thread {

    public static boolean listening = true;
    static String query = null;
    static Socket[] mysocks = new Socket[6];
    static String[] names = new String[6];
    static Socket temp_socket = null;
    static int count = 0;
    String discoectedClient = null;
    public static ServerSocket serverSocket = null;
    public ServerMain(){}
    
    public int  decrementCount(int clientNumber){
        count--;
        mysocks[clientNumber] = null;
        return count;
    }
    public static void main(String[] args) throws IOException{
        String pathname = ClassLoader.getSystemClassLoader().getResource(".").getPath();
        new File(pathname+"UserDataBase.txt").createNewFile();
        
        try (PrintWriter writer = new PrintWriter(new File(pathname+"UserDataBase.txt"))) {
            writer.println("1");
            writer.close();
        }
        for(int i = 0; i < 6; i++){
            mysocks[i] = null;
            names[i] = null;
        }
        try{
            serverSocket = new ServerSocket(16000);
        }catch(IOException e){
            System.err.println("Could not listen on port: 16000");
            System.exit(-1);
        }
        System.out.println("Server is run");
       	System.out.println("Adress "+serverSocket.getInetAddress());
       	System.out.println("Port "+serverSocket.getLocalPort());
        while(listening){

            if(count < 6 &&(temp_socket = serverSocket.accept())!=null){
                for(int i = 0; i < 6; i++){
                    if(mysocks[i] == null){
                        count = i;
                        mysocks[i] = temp_socket;
                        temp_socket = null;
                        new ServerClientConnect(mysocks[count],mysocks,count,names,query).start();
                        break;
                    }
                }
                count = 0;
                for(int i = 0; i < 6; i++){
                    if(mysocks[i]!= null){
                        count++;
                    }
                }
            }else{
                System.err.println("Server is full");
                break;
            }
            if(serverSocket.isClosed()){
                listening = false;
                System.exit(-1);
            }
        }
        serverSocket.close();
    }
}

