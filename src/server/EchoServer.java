package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Boundry.ServerGuiController;
import dataBase.DB_Connection;

import javafx.event.Event;
import logic.ClientConnected;

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
			private Map<ConnectionToClient,String> userNamesOfUsers = new HashMap<>();
			
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
					@SuppressWarnings("unchecked")
					ArrayList<String> data = (ArrayList<String>) msg;
					String userName;
					ArrayList<String> back = new ArrayList<>();
					back.clear();
					if(data.size()>0) {
						String action = data.get(0);
						switch (action) {
						case "quit":
								System.out.println("Message received: " + msg + " from " + client);
								data.remove(0);
								userName = userNamesOfUsers.get(client);
								userNamesOfUsers.remove(client);
								DB_Connection.signOutUser(userName);
								clientDisconnected(client);
								back.add("quit");
								client.sendToClient(back);
								data.clear();
								break;
							
						case "UserNameAndPasswordCheck":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> userNameAndPasswordRetVal = DB_Connection.checkUserNameAndPassword(data);
							if(!data.get(0).equals("Error")) {
								userNamesOfUsers.put(client, data.get(0));
							}
							userNameAndPasswordRetVal.add(0,"userNameAndPasswordRetVal");
							client.sendToClient(userNameAndPasswordRetVal);
							data.clear();
							break;
						case "SignOut":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							userName = userNamesOfUsers.get(client);
							userNamesOfUsers.remove(client);
							DB_Connection.signOutUser(userName);
							back.add("SignOut");
							client.sendToClient(back);
							data.clear();
							break;
						case "getUserData":
							userName = userNamesOfUsers.get(client);
							ArrayList<String> userData = DB_Connection.getUserData(userName);
							userData.add(0,"getUserData");
							client.sendToClient(userData);
							data.clear();
							break;
						case "CheckUserNameIsntExist":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> StatusOfUserName = DB_Connection.addNewCustomer(data);
							StatusOfUserName.add(0,"CheckUserNameIsntExist");
							client.sendToClient(StatusOfUserName);
							data.clear();
							break;
						
							/*case "insert":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							DB_Connection.saveSubscriber(data);
							data.clear();
							break;

						case "display":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> S = DB_Connection.getSubscriber(data);
							S.add(0,"display");
							client.sendToClient(S);	
							data.clear();
							break;

						case "update":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							DB_Connection.Update(data);
							back.add("update");
							client.sendToClient(back);
							data.clear();
							break;*/
							
						default:
							throw new Exception("Invalid operation");
						
					}
					
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