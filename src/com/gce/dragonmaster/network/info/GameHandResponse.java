package com.gce.dragonmaster.network.info;

import java.util.Vector;

import com.gce.dragonmaster.game.GameHand;
import com.gce.dragonmaster.network.GameServer;
import com.gce.dragonmaster.network.Player;

public class GameHandResponse extends ClientInfo {
	private static final long serialVersionUID = 1L;
	GameHand gameHand;
	
	public GameHandResponse(GameHand gameHand) {
		this.gameHand = gameHand;
	}
	
	
	@Override
	public void execute(GameServer gameServer, Player player) {
		Vector<Player> players = gameServer.getPlayers();
		
		gameServer.getHandsPlayed().put(gameHand, true);
		
		gameServer.setCurrentHand(gameHand);
		for (Player p : players) {
			p.write(new GameHandInfo(gameHand));
			p.write(new InformationInfo(String.format("%s: %s", gameHand, gameHand.getObjective())));
		}
	}
}
