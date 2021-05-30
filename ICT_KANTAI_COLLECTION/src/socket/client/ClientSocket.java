package socket.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import model.platform.Board;

public class ClientSocket {
	private static ClientSocket INSTANCE = new ClientSocket();
	
    final static String serverHost = "localhost";

    private Socket socketOfClient = null;
    private BufferedWriter os = null;
    private BufferedReader is = null;
    
    private Scanner scanner = new Scanner(System.in);
    
    private String username = "";
    private ClientState state;
    private String opponent = "";
    
    public ClientSocket() {
    	System.out.println("A new battlship online client");
//    	initSocket();
    }
    
    public static ClientSocket getInstance() {
    	return INSTANCE;
    }
    
    public ClientState getClientState() {
    	return state;
    }
    
    public void setClientState(ClientState state) {
    	this.state = state;
    }
    
    public String getUserInput() {
    	return scanner.nextLine().trim();
    }

	public void setState(ClientState state) {
    	this.state = state;
    }
    
    public void sendServer(String msg) {
    	
        try {
			os.write(msg);
			os.newLine(); 
	        os.flush();  
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Cannot communicate with the Server!!");
		} 
        
    }
    
    public String getServerMessage() {
    	String msg;
//    	try {
//			while ((msg = is.readLine()) != null) {
//				System.out.println("Server: " + msg);
//				return msg;
//			}
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    	
//    	return null;
    	try {
			msg = is.readLine();
			System.out.println("Server: " + msg);
			return msg;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public void listenServer() throws IOException {
        String responseLine;
        while ((responseLine = is.readLine()) != null) {
            System.out.println("Server: " + responseLine);
            
            if (responseLine.indexOf("login-1: ") == 0) {
            	username = responseLine.substring(9);
            	System.out.println("Welcome to Battleship Online, " + username);
            	break;
            }
        }
    }
    
	public boolean isValidUsername(String username) {
		char c;
		for (int i=0; i<username.length(); i++) {
			c = username.charAt(i);
			if ( !((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) )
				return false;	// if one of the character of the username is not a character
								// nor a digit then the username is invalid
		}
		return true;
	}

	public void initSocket() {
		
		try {
	           socketOfClient = new Socket(serverHost, 7777);
	 
	           os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
	 
	           is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
	 
		} catch (UnknownHostException e) {
	           System.err.println("Don't know about host " + serverHost);
	           return;
		} catch (IOException e) {
	           System.err.println("Couldn't get I/O for the connection to " + serverHost);
	           return;
		}
	 

	}

	public boolean handleMatching() {
		String reply;
		reply = getServerMessage();
		
		while (true) {
			if (reply.indexOf("continue?")==0) {	

				// check if user want to cancel matching
				if (state == ClientState.Matching)
					sendServer("y");
				else {
					sendServer("n");
					return false;
				}
					
				reply = getServerMessage();

				
			} else if (reply.indexOf("matchstart: ")==0) {
				// enter match ("matchstart: [userID]")
				this.opponent = reply.substring(12);
				System.out.println("Start playing. Your opponent is " + this.opponent);
				state = ClientState.Playing;
				break;
			}
		}
		
		return true;
	}
	
	public boolean handlePending() {
		String reply;
		reply = getServerMessage();
		
		while (true) {
			if (reply.indexOf("continue?")==0) {	

				// check if user want to cancel challenging
				if (state == ClientState.Pending)
					sendServer("y");
				else {
					sendServer("n");
					return false;
				}
					
				reply = getServerMessage();

				
			} else if (reply.indexOf("matchstart: ")==0) {	// challenge accepted
				// enter match ("matchstart: [userID]")
				this.opponent = reply.substring(12);
				System.out.println("Start playing. Your opponent is " + this.opponent);
				state = ClientState.Playing;
				break;
			} else {	// challenge declined
				state = ClientState.Idle;
				return false;
			}
		}
		
		return true;
	}

	public String handlePlayingState(String msg) {
		String input;
		boolean battleStage = false;
		while (true) {
			System.out.println("Enter command: ");
			input = getUserInput();
			sendServer(input);

			if (input.indexOf("quit") == 0) {
				break;
			}
			
			if (!battleStage) {	// Setup stage
				if (input.indexOf("setup: ") == 0) {
					// wait for server to send start match signal (TODO: loading screen)
					String startMessage = getServerMessage();
					if(startMessage.indexOf("gamestart: ") == 0) {
						battleStage = true;
						if (startMessage.indexOf("1") == 11) {
							System.out.println("Battle start! You go first.");
						} else {
							System.out.println("Battle start! Your opponent go first.");
							processOpponentAction();
						}
					}
				}
			} else {	// Battle stage
				// TODO: send action message
			}
			
		}
		
		return "";
	}
	
	public boolean logIn(String name) {
		if (!isValidUsername(name)) {	// check validity of username (TODO: move this step to server)
			return false;
		}
		
		try {
				initSocket();
			
	    	   	sendServer("login: " + name);
//	    	   	sendServer("New client connected at " + new Date());
	    	   	state = ClientState.Idle;
	           
	           listenServer();
	           	 
	           return true;
	           
		} catch (UnknownHostException e) {
	           System.err.println("Trying to connect to unknown host: " + e);
		} catch (IOException e) {
	           System.err.println("IOException:  " + e);
		}
		
		return false;
	}
	
	public ArrayList<String> getUserList() {
		ArrayList<String> userList = new ArrayList<String>();
		String reply;
		
		sendServer("getlist");
		reply = getServerMessage();
		while (reply.indexOf("userlist-done") != 0) {
			if (reply.indexOf("userlist: ") != -1) {
				
				// add online users to list until receive "userlist-done"
				userList.add(reply.substring(10));
				System.out.println(reply.substring(10));
				
			}
			reply = getServerMessage();
		}
		
		return userList;
	}
	
	public boolean matchRandom() {
		sendServer("random");
		state = ClientState.Matching;
		return handleMatching();
	}
	
	public boolean challenge(String opponent) {
		sendServer("challenge: " + opponent);
		state = ClientState.Pending;
		return handlePending();
	}
	
	public boolean responseChallenge (String opponent, boolean response) {
		if (response) {
			sendServer("accept: " + opponent);
			if (getServerMessage().indexOf("matchstart") == 0) {
				state = ClientState.Playing;
				this.opponent = opponent;
				return true;
			}
			
			System.out.println("Opponent missing");
		} else {
			sendServer("decline: " + opponent);
		}
		
		return false;
	}
	
	public void quitOnline() {
		
		sendServer("quit");
		
		String reply = getServerMessage();
		if (reply.indexOf("OK") != -1) {
			try {
	        	os.close();
				is.close();
				socketOfClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	public String setupShip(Board board) {
		sendServer("setup: V-4-2-3,H-2-1-1");
		
		// wait for server to send start match signal (TODO: loading screen)
		String startMessage = getServerMessage();
		if(startMessage.indexOf("gamestart: ") == 0) {
			if (startMessage.indexOf("1") == 11) {
				System.out.println("Battle start! You go first.");
				return "gamestart: 1";
			} else {
				System.out.println("Battle start! Your opponent go first.");
				return "gamestart: 2";
			}
		}
		
		return "";
	}

	public static void processOpponentAction() {
		// TODO: get server message (opponent's action) and process it
	}
}
