package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

import util.FileReceiver;

public class Client implements Runnable {  
	
	private static BufferedReader serverInput = null;
	private static BufferedReader in = null;
	private static PrintStream serverOutput = null;

	public static void main(String[] args) {

		try {
			Socket clientSocket = new Socket("localhost",3333); 
			serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
			serverOutput = new PrintStream(clientSocket.getOutputStream());  
			in = new BufferedReader(new InputStreamReader(System.in));
			
			new Thread(new Client()).start(); 
			String messageFromServer;
			
			while(true) {
				messageFromServer = serverInput.readLine();
				
				if(messageFromServer.startsWith("***quit")) {
					break;
				}
				
				if(messageFromServer.startsWith("Stigla je karta, proverite inbox")) {
					new FileReceiver(clientSocket).receiveFile("./tickets/"+UUID.randomUUID().toString()+".txt");
				}
				
				System.out.println(messageFromServer);
			}
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Server is down");
		}finally {
			System.exit(0); 
		}
	}
	
	@Override
	public void run() {

		try {
			String messageToServer;
			while(true){
				messageToServer = in.readLine();
				serverOutput.println(messageToServer);
				
				if(messageToServer.startsWith("Q")) {
					break;
				}
			}
		} catch (IOException ex) {
			System.out.println("Error with keyboard input stream");
		}catch(Exception e) {
			System.out.println("\nClient has been terminated!");
		}
	}

}
