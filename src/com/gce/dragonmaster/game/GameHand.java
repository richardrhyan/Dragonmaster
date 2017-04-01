package com.gce.dragonmaster.game;

import com.gce.dragonmaster.cards.Card;
import com.gce.dragonmaster.cards.Hand;
import com.gce.dragonmaster.cards.Suit;
import com.gce.dragonmaster.cards.Value;
import com.gce.dragonmaster.network.Player;


public enum GameHand {
	DRAGONLORDS ("Dragonlords", "Do not take any Dragonlords", 
			"Capture all Dragonlords", 1000, 10000, 8000),
	FIRST_OR_LAST ("First or Last", "Do not take the first or last trick",
			"Capture the first and last trick", 4000, 6000, 8000),
	RUNESWORD ("Runesword", "Do not take the Prince of Warriors",
			"Capture the Prince of Warriors", 8000, 6000, 8000),
	WIZARDS ("Wizards", "Do not take any Wizards",
			"Capture all Wizards", 2000, 10000, 8000),
	STAFF_OF_POWER ("Staff of Power", "Do not take any Dragonlords, Wizards, the Prince or Warriors, or the first or last trick",
			"Capture all Dragonlords, Wizards, the Prince or Warriors, and the first and last trick", 
			-1, 15000, 8000);
	
	private String name;
	private String objective;
	private String powerplayObjective;
	private int penalty;
	private int powerplayReward;
	private int powerplayPenalty;
	
	private GameHand(String name, String objective, String powerplayObjective, int penalty, 
			int powerplayReward, int powerplayPenalty) {
		this.name = name;
		this.objective = objective;
		this.powerplayObjective = powerplayObjective;
		this.penalty = penalty;
		this.powerplayReward = powerplayReward;
		this.powerplayPenalty = powerplayPenalty;
	}
	
	public	int	getCostOfHand(Player player) {
		int value = 0;
		Hand tricks = player.getTricks();
		
		if (this == GameHand.FIRST_OR_LAST || this == GameHand.STAFF_OF_POWER) {
			if (player.hasFirstTrick())
				value += 4000;
			if (player.hasLastTrick())
				value += 4000;
		}
		
		for (int i = 0; i < tricks.getSize(); i++) {
			Card c = tricks.getCard(i);
			if (this == GameHand.DRAGONLORDS || this == GameHand.STAFF_OF_POWER)
				if (c.getSuit().equals(Suit.DRAGONLORDS))
					value += 1000;
			
			if (this == GameHand.WIZARDS || this == GameHand.STAFF_OF_POWER)
				if (c.getValue().equals(Value.WIZARD))
					value += 2000;
			
			if (this == GameHand.RUNESWORD || this == GameHand.STAFF_OF_POWER)
				if (c.getSuit().equals(Suit.WARRIORS) && c.getValue().equals(Value.PRINCE))
					value += 8000;
		}
		return value;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getObjective() {
		return objective;
	}
	
	public String getPowerplayObjective() {
		return powerplayObjective;
	}
	
	public int getPenalty() {
		return penalty;
	}
	
	public int getPowerplayReward() {
		return powerplayReward;
	}
	
	public int getPowerplayPenalty() {
		return powerplayPenalty;
	}
	
	public static int size() {
		return values().length;
	}
}
