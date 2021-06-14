package socket.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import model.utils.MagicGenerator;
import socket.client.ClientState;

public class ClientHandler {
	private Socket socketOfServer;
	private int clientNumber;
	private BufferedReader is;
	private BufferedWriter os;
	private String userID = "";
	private ClientState clientState;
	private ClientHandler opponent = null;
	private GameMatchHandler match = null;
	private ArrayList<ClientHandler> challengerList = new ArrayList<ClientHandler>();
	
	public ClientHandler(Socket socketOfServer, int clientNumber) {
		this.socketOfServer = socketOfServer;
		this.clientNumber = clientNumber;
		this.clientState = clientState.Idle;
		
		
        try {
			is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
			os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
        
        String msg = getClientInput();
		
		// Get username
		if (msg.indexOf("login: ") == 0) {
			String name = msg.substring(7) + "#" + clientNumber;
			sendMessage("login-1: " + name);
			userID = name;
		}
		// TODO: handle else situation ?
        
	}
	
	public String getClientInput() {
		String msg = "";
		try {
			msg = is.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Oops! Cannot communicate with client!");
			

			ShipServer.getInstance().removeClient(this);
//			System.exit(0);
			

		}
		
		System.out.println("Client" + this.userID + " : " + msg);
		
		return msg;
	}
	
	public void sendMessage(String msg) {
		
        try {
			os.write(msg);
	        os.newLine();
	        os.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Oops! Cannot communicate with client!");
			

			ShipServer.getInstance().removeClient(this);
//			System.exit(0);

			
		} 
	}
	
	public void handleIdleUser() {
		while (true) {
            String msg = getClientInput();

            if (msg.indexOf("quit") == 0) {
                sendMessage(">> OK");
                ShipServer.getInstance().removeClient(this);
//                System.exit(0);
                break;
            }
            
            if (msg.indexOf("getlist") == 0) {
                sendUserList();
            }
            
            if (msg.indexOf("random") == 0) {
            	this.clientState = ClientState.Matching;
            	handleMatchingUser();
			}
            
            if (msg.indexOf("accept:") == 0) {
				
			}
			
			if (msg.indexOf("decline:") == 0) {
				
			}
			
			if (msg.indexOf("challenge:") == 0) {
				System.out.println(this.userID + " has challenged " + msg.substring(11));
				
				ClientHandler challengedPlayer = ShipServer.getInstance().getClient(msg.substring(11));
				
				challengedPlayer.addChallenger(this);
				
				// TODO: reply
				sendMessage("decline");
			}
            

        }
	}

	public void handleMatchingUser() {
		ClientHandler oppo;
		String msg = "";
		while (true) {
			// wait for 10 seconds then try to find another matching user
			try {
    			TimeUnit.SECONDS.sleep(3);
    		} catch (InterruptedException e) {
    			System.out.println("Thread is interuppted....");
    		}
			
			if (this.opponent != null) {
				break;				
			}
			
			oppo = getMatchingUser();
			
            if (oppo != null) {
            	this.opponent = oppo;
            	oppo.setOpponent(this);
            	System.out.println("A match created between " + this.userID + " and " + this.opponent.getUserID());
            	createMatch();
            	break;
            }
            
            sendMessage("continue?");
            msg = getClientInput();
            if (msg.indexOf("n") == 0) 
            	break;

        }
		
		if (this.opponent != null) {		
			sendMessage("matchstart: " + this.opponent.userID);
			this.clientState = ClientState.Playing;
			handlePlayingUser();
			this.clientState = ClientState.Idle;	// return to Idle after playing
			this.opponent = null;
			this.match = null;
		} else {
			this.clientState = ClientState.Idle;
		}
	}

	public void createMatch() {
		System.out.println("A match created between " + this.userID + " and " + this.opponent.getUserID());
		this.match = new GameMatchHandler (this.userID, this.opponent.getUserID());
		this.opponent.joinMatch(this.match);
	}
	
	public void joinMatch(GameMatchHandler match) {
		this.match = match;
	}

	public void handlePlayingUser() {
		String msg;
		while (true) {
			msg = getClientInput();
			if (msg.indexOf("quit") == 0) {
				break;
			}
			
			if (match.isBattleStage() == false) {	// Setup Stage
				if (msg.indexOf("setup: ") == 0) {
					System.out.println("Received ship formation from " + this.userID + ":");
					this.match.processShipSetUp(this.userID, msg.substring(7));
					
					// wait until server receive formation from both players
					while (true) {
						try {
							TimeUnit.SECONDS.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (match.isBattleStage() == true)
							break;
					}
					
					if (match.getCurrentTurnPlayerID().equals(this.userID)) {
						sendMessage("gamestart: 1");
					} else {
						sendMessage("gamestart: 2");
					}
				}
			} else {	// Battle Stage
				// process shooting message
				if (msg.indexOf("fire: ") == 0) {
					String[] cellCoordinate = msg.substring(6).split("-");
					System.out.println(this.userID + " fire at cell: " + cellCoordinate[0] + "," + cellCoordinate[1]);
					String result = match.processPlayerFire(userID, Integer.parseInt(cellCoordinate[0]), Integer.parseInt(cellCoordinate[1]));
					sendMessage(result);
				}
			}
			
		}
		
	}

	public void processShipSetUp(String formation) {
		String[] shipLocations = formation.split(",");	// split into strings containing location of each ship on board
		String[] params;	
		String shipDirection;
		for (String s: shipLocations) {
			params = s.split("-");
			if (params[0].indexOf("V")==0)
				shipDirection = "vertical";
			else
				shipDirection = "horizontal";
			System.out.println("Ship: " + shipDirection + ", length=" + params[1] + ", x=" + params[2] + ", y=" + params[3]);
		}
	}

	public ClientHandler getMatchingUser() {
		System.out.println("check matching list");
		ArrayList<ClientHandler> matchingPool = new ArrayList<ClientHandler>();
		int index;
		
		for (ClientHandler client: ShipServer.getInstance().getClientList()) {
			if (!client.equals(this)) {
				if (client.getState() == ClientState.Matching && client.getOpponent() == null)
					matchingPool.add(client);
			}
		}
		
		if (matchingPool.size() > 0) {
			index = MagicGenerator.getRandInt(matchingPool.size());
			return matchingPool.get(index);
		}
		
		return null;
	}

	public void sendUserList() {
		String state;
		
		for (ClientHandler client: ShipServer.getInstance().getClientList()) {
			if (!client.equals(this)) {
				if (client.getState() == ClientState.Playing)
					state = "playing";
				else
					state = "idle";
				sendMessage("userlist: " + client.getUserID() + " / " + state);
			}
		}
		
		sendMessage("userlist-done");
	}

	public String getUserID() {
		return userID;
	}
	
	public ClientState getState() {
		return this.clientState;
	}
	
	public ClientHandler getOpponent() {
		return this.opponent;
	}
	
	public void setOpponent(ClientHandler opponent) {
		this.opponent = opponent;
	}
	
	public void addChallenger(ClientHandler user) {
		challengerList.add(user);
	}
	
	public void removeChallenger(ClientHandler user) {
		challengerList.remove(user);
	}
	
	public ArrayList<ClientHandler> getChallengerList () {
		return this.challengerList;
	}
}
