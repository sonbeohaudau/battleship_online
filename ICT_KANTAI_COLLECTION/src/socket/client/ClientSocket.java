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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javafx.geometry.Orientation;
import model.platform.Board;
import model.system.GameConfig;
import model.unit.warship.Ship;

public class ClientSocket {
	private static ClientSocket INSTANCE = new ClientSocket();
	
    final static String serverHost = "localhost";

    private Socket socketOfClient = null;
    private BufferedWriter os = null;
    private BufferedReader is = null;
    private ClientExtraSocket extraSocket = new ClientExtraSocket();
    
    private Scanner scanner = new Scanner(System.in);
    
    private String username = "";
    private ClientState state = ClientState.Disconnected;
    private String opponent = "";
    
    private boolean goFirst = true;
    
    public ClientSocket() {
    	System.out.println("A new battlship online client");
//    	initSocket();
//    	extraSocket.start();
    	
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
    
    public String getOpponent() {
    	return opponent;
    }
    

    public String getUserInput() {
    	return scanner.nextLine().trim();
    }
	
	public boolean isGoFirst() {
		return this.goFirst;
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
    
    public String getServerReply() {
    	return extraSocket.getServerReply();
    }
    
    public String getServerMessage() {
    	String msg = "";
    	
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
		reply = getServerReply();
		
		while (true) {
			if (reply.indexOf("continue?")==0) {	

				// check if user want to cancel matching
				if (state == ClientState.Matching)
					sendServer("y");
				else {
					sendServer("n");
					return false;
				}
					
				reply = getServerReply();

				
			} else if (reply.indexOf("matchstart: ")==0) {
				// enter match ("matchstart: [userID]")
				this.opponent = reply.substring(12);
				System.out.println("Start playing. Your opponent is " + this.opponent);
				state = ClientState.Playing;
				break;
			}
		}
		
		System.out.println(13);
		
		return true;
	}
	
	public boolean handlePending() {
		String reply;
		reply = getServerReply();
		
		while (true) {
			if (reply.indexOf("continue?")==0) {	

				// check if user want to cancel challenging
				if (state == ClientState.Pending)
					sendServer("y");
				else {
					sendServer("n");
					return false;
				}
					
				reply = getServerReply();

				
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
	
	public void enterMatch(String opponent) {
		this.opponent = opponent;
		System.out.println("Start playing. Your opponent is " + this.opponent);
		state = ClientState.Playing;
		
		GameConfig.getOnlineLobby().goToGameWindow();
	}

//	public String handlePlayingState(String msg) {
//		String input;
//		boolean battleStage = false;
//		while (true) {
//			System.out.println("Enter command: ");
//			input = getUserInput();
//			sendServer(input);
//
//			if (input.indexOf("quit") == 0) {
//				break;
//			}
//			
//			if (!battleStage) {	// Setup stage
//				if (input.indexOf("setup: ") == 0) {
//					// wait for server to send start match signal (TODO: loading screen)
//					String startMessage = getServerMessage();
//					if(startMessage.indexOf("gamestart: ") == 0) {
//						battleStage = true;
//						if (startMessage.indexOf("1") == 11) {
//							System.out.println("Battle start! You go first.");
//						} else {
//							System.out.println("Battle start! Your opponent go first.");
//							processOpponentAction();
//						}
//					}
//				}
//			} else {	// Battle stage
//				// TODO: send action message
//			}
//			
//		}
//		
//		return "";
//	}
	
	public boolean logIn(String name) {
		if (!isValidUsername(name)) {	// check validity of username (TODO: move this step to server)
			
			System.out.println("Invalid username!");
			return false;
		}
		
		initSocket();
		extraSocket.start();
		
		sendServer("login: " + name);

        // get name tag from server and add to player's name
        String response = getServerReply();
            
        if (response.indexOf("login-1: ") == 0) {
        	state = ClientState.Idle;
        	username = response.substring(9);
			System.out.println("Welcome to Battleship Online, " + username);
			return true;
        }
		 
        return false;
		
	}
	
//	public ArrayList<String> getUserList() {
//		ArrayList<String> userList;
//		String reply;
//		
//		sendServer("getlist");
//		reply = getServerReply();
//		String[] users = reply.substring(10).split(",");
//		userList = new ArrayList<String>(Arrays.asList(users));
//		
////		while (reply.indexOf("userlist-done") != 0) {
////			if (reply.indexOf("userlist: ") != -1) {
////				
////				// add online users to list until receive "userlist-done"
////				userList.add(reply.substring(10));
////				System.out.println(reply.substring(10));
////				
////			}
////			reply = getServerReply();
////		}
//		
//		return userList;
//	}
	
	public void getUserList() {
		sendServer("getlist");
	}

	
	public void getChallengeList() {
		sendServer("getchallengelist");
	}

	public void updateUserList(String msg) {
		String[] users = msg.substring(10).split(",");
		ArrayList<String> userList = new ArrayList<String>(Arrays.asList(users));
		
		GameConfig.getOnlineLobby().updatePlayerList(userList);
	}
	
	public void updateChallengeList(String msg) {
		String[] challenges = msg.substring(15).split(",");
		ArrayList<String> challengeList = new ArrayList<String>(Arrays.asList(challenges));
		
		GameConfig.getOnlineLobby().updateChallengeList(challengeList);
	}
	
//	public boolean matchRandom() {
//		sendServer("random");
//		state = ClientState.Matching;
//		return handleMatching();
//	}
	
	public void matchRandom() {
		
		state = ClientState.Matching;
		sendServer("random");
		
	}
	
	public void challenge(String opponent) {
		System.out.println("Challenging " + opponent);
		sendServer("challenge: " + opponent);
		state = ClientState.Pending;
//		return handlePending();
	}
	
	public void responseChallenge (String opponent, boolean response) {
		if (response) {
			sendServer("accept: " + opponent);
			
		} else {
			sendServer("decline: " + opponent);
		}
		
	}
	
	public void processDeclinedChallenge() {
		state = ClientState.Idle;
		// TODO
	}
	
	public void quitOnline() {
		
		sendServer("quit");
		
		String reply = getServerReply();
		if (reply.indexOf("OK") != -1) {
			state = ClientState.Disconnected;
			try {
	        	os.close();
				is.close();
				socketOfClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	public boolean setupShip(Board board) {
//		sendServer("setup: V-4-2-3,H-2-1-1");
		StringBuffer setUpMsg = new StringBuffer();
		setUpMsg.append("setup: ");
		List<Ship> shipList = board.getShipArmy();
		for (Ship s: shipList) {
			// for each ship, send its orientation, length and coordination
			if (s.getOrien() == Orientation.HORIZONTAL) {
				setUpMsg.append("H-");
			} else {
				setUpMsg.append("V-");
			}
			
			setUpMsg.append(s.getShipLength());
			setUpMsg.append('-');
			setUpMsg.append(s.getCellList().get(0).getXPosition());
			setUpMsg.append('-');
			setUpMsg.append(s.getCellList().get(0).getYPosition());
			setUpMsg.append(',');
			
		}
		
		sendServer(setUpMsg.toString());
		
		// wait for server to send start match signal (TODO: loading screen)
		String startMessage = getServerReply();
		if(startMessage.indexOf("gamestart: ") == 0) {
//			extraSocket.start();
//			extraSocket.pause();
			
			if (startMessage.indexOf("1") == 11) {
				System.out.println("Battle start! You go first.");
				goFirst = true;
				return true;
			} else {
				System.out.println("Battle start! Your opponent go first.");
				goFirst = false;
				return true;
			}
		}
		
		return false;
	}
	
	public String fire (int x, int y) {
		sendServer("fire: " + x + "-" + y);
		
		String result = getServerReply();
		
		return result;
		
//		return "";
	}

	public void surrender() {
		sendServer("surrender");
	}
	
	
}
