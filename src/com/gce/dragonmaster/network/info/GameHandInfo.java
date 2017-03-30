package com.gce.dragonmaster.network.info;

import com.gce.dragonmaster.game.GameHand;
import com.gce.dragonmaster.gui.frames.GameFrame;

public class GameHandInfo extends ServerInfo {
	private static final long serialVersionUID = 1L;
	private GameHand gameHand;
	
	public GameHandInfo(GameHand gameHand) {
		this.gameHand = gameHand;
	}
	
	@Override
	public void execute(GameFrame gameFrame) {
		gameFrame.setTitle(String.format("Dragonmaster: %s hand", gameHand));
		gameFrame.getGamesPanel().setGamePlayed(gameHand);
	}

}
