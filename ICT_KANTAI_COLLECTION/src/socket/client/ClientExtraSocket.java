package socket.client;

import java.util.concurrent.TimeUnit;

import model.system.GameConfig;
import socket.server.ClientHandler;

public class ClientExtraSocket extends Thread{
	
	private String serverReply = "";
	
	@Override
    public void run() {
 	   
		while (true) {
			String msg = ClientSocket.getInstance().getServerMessage();
		 	processMsg(msg);
		}

    }

	private void processMsg(String msg) {
		
		if (msg.indexOf("fire:") == 0) {
			GameConfig.getGameMatch().processOppoFire(msg);
			return;
		}
		
		// if the msg not belong to above categories, save to variable serverReply so the ClientSocket
		// could get it
		serverReply = msg;
		
	}
	
	public String getServerReply() {
		while (serverReply.length() < 1) {
			try {
//				TimeUnit.SECONDS.sleep(1);
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			
		String msg = serverReply;
		serverReply = "";
		return msg;
	}
	
//	public void pause() {
//		
//		synchronized (this){
//			try{
//				wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//		    }
//		}
//		
//	}
//	
//	public void unpause() {
//		synchronized (this){
//			notify();
//		}
//	}
}
