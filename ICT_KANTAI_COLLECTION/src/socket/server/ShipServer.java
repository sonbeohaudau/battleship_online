package socket.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ShipServer {
	private static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
	
	public static ClientHandler getClient(String userID) {
		for (ClientHandler client: clients) {
			if (client.getUserID().equals(userID)) {
				return client;
			}
		}
		
		return null;
	}
	
//	private static void echoToClient(int clientNumber) {
//		try {
//			clients.get(clientNumber).listenAndEcho();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void echoToClient(String userID) {
		getClient(userID).handleIdleUser();
	}
	
	public static void sendClient(int clientNumber, String msg) {
		clients.get(clientNumber).sendMessage(msg);
	}
	
	public static void broadcastClients(String msg) {
		for (ClientHandler client: clients) {
			client.sendMessage(msg);
		}
	}
	
	public static ArrayList<ClientHandler> getClientList() {
		return clients;
	}
	
	public static void removeClient(ClientHandler client) {
		clients.remove(client);
	}

	public static void main(String[] args) throws IOException {
		ServerSocket listener = null;
		 
		System.out.println("Server is waiting to accept user...");
	       int clientNumber = 0;
	 
	 
	       try {
	           listener = new ServerSocket(7777);
	       } catch (IOException e) {
	           System.out.println(e);
	           System.exit(1);
	       }
	 
	       try {
	           while (true) {
	 
	 
	               // a client connected to the server
	        	   // create a thread to handle that client
	 
	               Socket socketOfServer = listener.accept();
	               new ServiceThread(socketOfServer, clientNumber++).start();
	           }
	       } finally {
	           listener.close();
	       }
	}
	 
	private static void log(String message) {
	       System.out.println(message);
	}
	 
	private static class ServiceThread extends Thread {
		 
	       private int clientNumber;
	       private Socket socketOfServer;
	 
	       public ServiceThread(Socket socketOfServer, int clientNumber) {
	           this.clientNumber = clientNumber;
	           this.socketOfServer = socketOfServer;
	 
	           log("New connection with client# " + this.clientNumber + " at " + socketOfServer);
	       }
	 
	       @Override
	       public void run() {
	    	   ClientHandler client = new ClientHandler(socketOfServer, clientNumber);
	    	   clients.add(client);
	           echoToClient(client.getUserID());
	       }
	}
	

}
