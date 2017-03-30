package com.gce.dragonmaster.network;

import java.util.Vector;

import com.gce.dragonmaster.network.info.ClientInfo;
import com.gce.dragonmaster.network.requests.ClientRequest;
import com.rhyan.requests.Request;

public class MyConnectionListener extends Thread {
	private GameServer		gameServer;
	private Vector<Player>	players;
	
	public MyConnectionListener(GameServer gameServer, Vector<Player> players) {
		this.gameServer = gameServer;
		this.players = players;
	}
	
	public void run() {
		while(!gameServer.isGameOver()) {
			for (int i = 0; i < players.size(); i++) {
				Player player = (Player) players.get(i);

//				if(!player.isAlive()) {
//					players.remove(i);
//				}
									
				Object message = player.getMessage();
				
				if (message != null) {
					if(message instanceof ClientRequest) {
						Object response = processRequest((ClientRequest)message, player);
						for (Player p : players) {
							p.write(response);
						}
					} else if(message instanceof ClientInfo) {
						processRequest((ClientInfo)message, player);
					
					} else {
						System.err.println("Server received unknown information from the client");
				}

	            // don't monopolize processor
	            try                 { Thread.sleep(100);   }
	            catch (Exception e) { e.printStackTrace(); }
				}
			}
		}
	}
	
	public synchronized Object processRequest(Request request, Player player) {
		if (request instanceof ClientRequest) {
			return ((ClientRequest) request).execute(gameServer, player);
		}
		if (request instanceof ClientInfo)
			((ClientInfo) request).execute(gameServer, player);
		
		return null;
	}
}
