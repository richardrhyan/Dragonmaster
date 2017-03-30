package com.gce.dragonmaster.network.requests;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.gce.dragonmaster.game.GameHand;
import com.gce.dragonmaster.gui.frames.GameFrame;
import com.gce.dragonmaster.network.info.GameHandResponse;

public class GameHandRequest extends ServerRequest {
	private static final long serialVersionUID = 1L;
	private Map<GameHand, Boolean>	handsPlayed = new TreeMap<GameHand, Boolean>();
	
	public GameHandRequest(Map<GameHand, Boolean> handsPlayed) {
		// For some reason, the HashMap does not get sent with the correct values
		// Creating a new Hashmap fixes this, but needs to be corrected
		List<GameHand>	keys		= new ArrayList<GameHand>(handsPlayed.keySet());
		for (GameHand gh : keys) {
			this.handsPlayed.put(gh, handsPlayed.get(gh));
		}
	}
	
	@Override
	public Object execute(GameFrame gameFrame) {
//		List<GameHand>	keys		= new ArrayList<GameHand>(handsPlayed.keySet());
//		List<GameHand>	available	= new ArrayList<GameHand>();
//		
//		for (GameHand gh : keys)
//			if (!handsPlayed.get(gh))
//				available.add(gh);
//		
		
		gameFrame.getInformationPanel().addMessage("Please select a game to play...", Color.RED);
		gameFrame.getGamesPanel().setGamePlayed(null);
		return new GameHandResponse(gameFrame.getGamesPanel().gameSelection());

//		GameHand selection = (GameHand)JOptionPane.showInputDialog(
//				gameFrame,
//				"Look at your hand and select a game to play",
//				"Select Game",
//				JOptionPane.QUESTION_MESSAGE,
//				null,
//				available.toArray(new GameHand[available.size()]),
//				available.get(0));
//		return new GameHandResponse(selection);
	}

}
