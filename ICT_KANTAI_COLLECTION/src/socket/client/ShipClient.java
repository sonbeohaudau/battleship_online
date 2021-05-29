package socket.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Scanner;

public class ShipClient {
	private static ShipClient INSTANCE = new ShipClient();
	
    final static String serverHost = "localhost";

    static Socket socketOfClient = null;
    static BufferedWriter os = null;
    static BufferedReader is = null;
    
    static Scanner scanner = new Scanner(System.in);
    
    static String username = "";
    static ClientState state;
    
    private ShipClient() {
    	System.out.println("A new battlship online client");
    }
    
    public static ShipClient getInstance() {
    	return INSTANCE;
    }
    
    public static String getUserInput() {
    	return scanner.nextLine().trim();
    }
    
    private static void sendServer(String msg) {
    	
        try {
			os.write(msg);
			os.newLine(); 
	        os.flush();  
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Cannot communicate with the Server!!");
		} 
        
    }
    
    public static String getServerMessage() {
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
    
    private static void listenServer() throws IOException {
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
    
    public static void chooseAnUsername() {
    	String username = "";
    	do {
    		System.out.print("Input username (only characters and digits allowed): ");
    		username = scanner.nextLine();
    	} while (!isValidUsername(username));
    	
    	
    	ShipClient.username = username;

    }
    
	private static boolean isValidUsername(String username) {
		char c;
		for (int i=0; i<username.length(); i++) {
			c = username.charAt(i);
			if ( !((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) )
				return false;	// if one of the character of the username is not a character
								// nor a digit then the username is invalid
		}
		return true;
	}

	public static void main(String[] args) {
		chooseAnUsername();
		
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
	 
		try {
	    	   sendServer("login: " + username);
	           sendServer("New client connected at " + new Date());
	           state = ClientState.Idle;
	           
//	           sendServer("QUIT");
	           
	           listenServer();
	           
	           handleIdleState();
	 
	           os.close();
	           is.close();
	           socketOfClient.close();
		} catch (UnknownHostException e) {
	           System.err.println("Trying to connect to unknown host: " + e);
		} catch (IOException e) {
	           System.err.println("IOException:  " + e);
		}

	}

	private static void handleIdleState() {
		String input;
		String reply;
		boolean idle = true;
		while (idle) {
			System.out.println("Enter command: ");
			input = getUserInput();
			sendServer(input);
			
			
			if (input.indexOf("quit") == 0) {
				reply = getServerMessage();
				if (reply.indexOf("OK") != -1) {
	                idle = false;
	            }
			}
			
			if (input.indexOf("getlist") == 0) {
				reply = getServerMessage();
				while (reply.indexOf("userlist-done") != 0) {
					if (reply.indexOf("userlist: ") != -1) {
						System.out.println(reply.substring(10));
					}
					reply = getServerMessage();
				}
			}
			
			if (input.indexOf("random") == 0) {
				state = ClientState.Matching;
				handleMatchingState();
				state = ClientState.Idle;
			}
			
			if (input.indexOf("accept:") == 0) {
				
			}
			
			if (input.indexOf("decline:") == 0) {
				
			}
			
			if (input.indexOf("challenge:") == 0) {
				
			}
			
			
		}
		
	}
	
	private static void handleMatchingState() {
		String reply;
		reply = getServerMessage();
		
		while (true) {
			if (reply.indexOf("continue?")==0) {
//				input = getUserInput();
//				if (input.indexOf("n") == 0)
//					break;	// go back to idle state
//				// else continue matching
//				else {
					sendServer("y");
//					reply = getServerMessage();
//				}

				
			} else if (reply.indexOf("matchstart: ")==0) {
				// enter match ("matchstart: [userID]")
				System.out.println("Start playing. Your opponent is " + reply.substring(12));
				state = ClientState.Playing;
				handlePlayingState();
				break;
			}
		}
	}

	private static void handlePlayingState() {
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
	}

	private static void processOpponentAction() {
		// TODO: get server message (opponent's action) and process it
	}
}
