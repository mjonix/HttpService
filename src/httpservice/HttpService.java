/**
* @The goal of this application is to provide HTTP service that would listen on port 80. Application
* @sends HTTP GET request to google.com and prints out the response. To test the application, compile
* @this file, then open any Internet browser and go to localhost:80, where you should see a response
* @of HTTP GET request to google.com in a plain text format. An updated result will be provided every
* @time you reload the page, as application use threads to allow multiple requests at the same time.
* 
* @author Marius Jonikas
*/
package httpservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class HttpService {
    
    public static void main(String[] args) throws IOException{
        ServerSocket server = new ServerSocket(80); 
        while(true){
            Socket socket = null; 
            try{
                socket = server.accept(); 
                System.out.println("New conncetion: " + socket);   
  
                /**
                * @Creating a thread to allow multiple simultaneous connections on a same port.
                */
                Thread thread = new MultipleRequests(socket); 
                thread.start(); 
            }catch (Exception e){
                socket.close(); 
            }
        }
    }
}

class MultipleRequests extends Thread{
    
    Socket socket =null;
    
    MultipleRequests(Socket socket){
        this.socket = socket;
    }
    
    @Override
    public void run(){
        try{
            OutputStream stream = socket.getOutputStream();
            socket = new Socket(InetAddress.getByName("www.google.com"), 80);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder result = new StringBuilder();
            PrintWriter writerGet = new PrintWriter(socket.getOutputStream(), true), 
                    writerDisplay = new PrintWriter(stream);
            writerGet.println("GET / HTTP/1.1");
            writerGet.println("Host: www.google.com:80");
            writerGet.println("Connection: Close");
            writerGet.println();
            /**
             * @Reading data from a response to GET request.
             */
            while(true)
                if(reader.ready()){
                    int i = 0;
                    while (i!=-1){
                        i = reader.read();
                        result.append((char)i);
                    }
                    break;
                }
            /**
             * @Displaying data on a web page.
             */
            writerDisplay.println("HTTP/1.0 200 OK");
            writerDisplay.println("Content-Length: "+(result.toString().length()+29));
            writerDisplay.println("");
            writerDisplay.println("GET request from google.com:\n\n"+result.toString());
            writerDisplay.flush();
            socket.close();
        }catch (IOException e){
            Logger.getLogger(MultipleRequests.class.getName()).log(Level.SEVERE, null, e);
        }
    } 
}