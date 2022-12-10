package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;


import Boundry.ServerGuiController;
import dataBase.DB_Connection;
import javafx.event.Event;
import logic.ClientConnected;
import logic.Subscriber;

import java.sql.Connection;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EchoServer extends AbstractServer {
	// Class variables *************************************************
			static Connection conn;
			private ArrayList<ConnectionToClient> clientConnected = new ArrayList<>();
			private String host;
			private InetAddress ip;
			final public static int DEFAULT_PORT = 5555;
			String DBPassword;
			
			
		//****************************************************************	
			
			
			/**
			 * Constructor , getting port and Starting the server  
			 * @param port
			 */
			public EchoServer(int port) {
				super(port);
				
				try {
					ip = InetAddress.getLocalHost();
					host = ip.getHostName();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			
			/**
			 * function to handle client request, 
			 * getting the request to action by "msg" and getting the specific client that ask the action
			 * In the end of each action , sending info back to client message handler 
			 * @param msg
			 * @param client
			 */
			
			public void handleMessageFromClient(Object msg, ConnectionToClient client) {
				try {
					
					ArrayList<String> data = (ArrayList<String>) msg;
					ArrayList<String> back = new ArrayList<>();
					
					String action = data.get(0);
					switch (action) {
					case "insert":
						System.out.println("Message received: " + msg + " from " + client);
						data.remove(0);
						DB_Connection.saveSubscriber(data);
						break;

					case "display":
						System.out.println("Message received: " + msg + " from " + client);
						data.remove(0);
						ArrayList<String> S = DB_Connection.getSubscriber(data);
						S.add(0,"display");
						client.sendToClient(S);										
						break;

					case "update":
						System.out.println("Message received: " + msg + " from " + client);
						data.remove(0);
						DB_Connection.Update(data);
						back.add("update");
						client.sendToClient(back);
						break;
					case "quit":
						System.out.println("Message received: " + msg + " from " + client);
						data.remove(0);
						clientDisconnected(client);
						back.add("quit");
						client.sendToClient(back);
						
					default:
						throw new Exception("Invalid operation");
					}

				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}
			
			
			/**
			 * getter for ip
			 */
			
			public InetAddress getIp() {
				return ip;
			}


			/**
			 * set ip 
			 * @param ip
			 */
			public void setIp(InetAddress ip) {
				this.ip = ip;
			}

			/**
			 * getter for host
			 */
			public String getHost() {
				return host;
			}
			
			/**
			 * set host 
			 * @param host
			 */
			public void setHost(String host) {
				this.host = host;
			}

			/**
			 * Function that started when user get connected and connecting the db
			 * @param dbName
			 * @param dbUserNameRoot
			 * @param dbPassword
			 */
			public void dbStarted(String dbPassword,String dbUserNameRoot, String dbName) {
				DB_Connection.connectDB(dbPassword,dbUserNameRoot,dbName);
			}
			
			
			//Function that start when server is starting
			@Override
			protected void serverStarted() {
				System.out.println("Server listening for connections on port " + getPort());


			}

			//Function that start when server is stop
			@Override
			protected void serverStopped() {
				System.out.println("Server has stopped listening for connections.");
			}
			
			
			
			/**
			 * function that called each time that client connect to server , and adding it to the table view gui
			 * @param client
			 */
			@Override
		    public void clientConnected(ConnectionToClient client) {
				boolean isAdded = false;
				ServerUI serverUI = ServerUI.getInstance();
				
				for(ConnectionToClient clientTemp : clientConnected) {
					if(clientTemp.getInetAddress().getHostName().equals(client.getInetAddress().getHostName())) {
						isAdded = true;
						break;
					}
				}
				
				if(!isAdded) {
					clientConnected.add(client);
					serverUI.tableViewList.add(new ClientConnected(client.getInetAddress().getHostAddress(),client.getInetAddress().getHostName(),"Connected"));
				}
				
		    }
			
			
			/**
			 * function that called each time that client Disconnect from server , and remove it to from table view GUI
			 * @param client
			 */
			@Override
			public void clientDisconnected(ConnectionToClient client) {
				for(ConnectionToClient clientTemp : clientConnected) {
					if(clientTemp.getInetAddress().getHostName().equals(client.getInetAddress().getHostName())) {
						clientConnected.remove(clientTemp);
						break;
					}
				}
				ServerUI serverUI = ServerUI.getInstance();
				serverUI.tableViewList.clear();
				serverUI.tableViewList.add(new ClientConnected(client.getInetAddress().getHostAddress(),client.getInetAddress().getHostName(),"Disconnected"));
				
		    }

}